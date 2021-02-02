window.addEventListener('popstate', function (event) {
    history.pushState(null, null, 'home.jsp');
});

$(document).ready(function () {
    $(window).bind("beforeunload", function () {
        $.ajax({
            type: 'GET',
            async: false,
            url: 'removeLocks',
            error: function (e) {
                console.log(e);
            }
        });
    });
    call_startup();
});
let summernote_options = {
    height: 300,
    toolbar: [
        ['font', ['bold', 'italic', 'underline', 'clear']],
        ['fontname', ['fontname']],
        ['fontsize', ['fontsize']],
        ['color', ['color']],
        ['para', ['ul', 'ol', 'paragraph']],
        ['height', ['height']],
        ['insert', ['hr']],
        ['view', ['fullscreen']],],
    disableDragAndDrop: true,
};
let categories = [];
let imageList = [];
let comments = [];
let comment = "";
let contentId = '';
let editable = true;
let role = '';
let categoryVal = '';
let title = '';
let content = '';
let source = '';
let empRole = '';
let navigation = "";
let dateObj = new Date();
let monthArray = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
let month = monthArray[dateObj.getMonth()]; //months from 1-12
let year = dateObj.getUTCFullYear();
newDate = month + " " + year;
let status = "NEW";
let data = "";
mode = true;

function clear() {
    imageList = [];
    comments = [];
    comment = "";
    contentId = '';
    editable = true;
    role = '';
    categoryVal = '';
    title = '';
    content = '';
    source = '';
    status = "NEW";
    mode = true;
    $("#oldComments").hide();
    $('#create-category').prop('disabled', false);
    $("#title-create,#source-create").prop('disabled', false);
    $("#new-category").show();
    $("#images").show();
    $('.note-editable').attr('contenteditable', true);
    $("#comments-view").show();
    return false;
}

function dialogs() {
    // Image Upload dialog box
    $("#dialog-create").dialog({
        autoOpen: false,
        title: "Image Upload",
        height: 300,
        width: 300,
        dialogClass: "imgUpload"
    });

    // Image View after click on thumbnail
    $("#showImg").dialog({
        autoOpen: false,
        dialogClass: "showImage"
    });

    // New Category dialog box
    $("#category-dialog").dialog({
        autoOpen: false,
        open: function (event, ui) {
            $(event.target).parent().css('position', 'fixed');
            $(event.target).parent().css('top', '203px');
            $(event.target).parent().css('left', '435px');
        },
        dialogClass: "newCat"
    });
    return false;
}

function call_startup() {
    if (empRole === "ADMIN")
        startupAdmin();
    else
        startup();
    return;
}

function startup() {

    $("#tabs").tabs(
        {
            collapsible: true,
            active: false,
            activate: function (event, ui) {
                if ($('#tabs .ui-state-active').attr('aria-controls') === "viewTab") {
                    $("#dialog-create").dialog('close');
                    $("#showImg").dialog('close');
                    $("#category-dialog").dialog('close');
                    fillData();
                } else if ($('#tabs .ui-state-active').attr('aria-controls') === "New") {
                    $("#dialog-create").dialog('close');
                    $("#showImg").dialog('close');
                    $("#category-dialog").dialog('close');
                    clear();
                    navigation = "create";
                    bind("");
                    if(form) {
                        $("#New").html("");
                        form.appendTo("#New");
                        form = null
                    }
                }
            }
        }
    )
    ;

    bind("");
    fillCategories(categoryVal);
    $("#title-create").val(title);
    $("#source-create").val(source);
    $("#category-validation").hide();
    $("#title-validation").hide();
    $("#comment-validation").hide();
    $("#content-validation").hide();
    $("#dvPreview").html("");
    $("#postedComment").hide();
    $("textarea#commentText").val('');
    $("#newComment").show();
    $('.summernote').summernote(summernote_options);
    $('.summernote').summernote('reset');
    $("#oldComments").html("");

    $("#source-display-create").hide();
    $("#btn-display-create").hide();
    $("#title-display-create").hide();
    $("#grid-create").hide();

    let welcome = $("#welcome");
    welcome.html("Welcome " + username);
    fillComments();

    dialogs();

    $('#title-create').on('input', function () {
        if (document.getElementById('title-create').value != '') {
            document.getElementById('grid-create').style.display = "block";
            document.getElementById('source-display-create').style.display = "block";
            document.getElementById('btn-display-create').style.display = "block";
            return;
        } else {
            document.getElementById('grid-create').style.display = "none";
            document.getElementById('source-display-create').style.display = "none";
            document.getElementById('btn-display-create').style.display = "none";
        }

    });

    $('#create-category').on('change', function () {
        if (document.getElementById('create-category').value != '')
            document.getElementById('title-display-create').style.display = "block";
    });

    if (navigation === "view") {
        $("#tabs").tabs("option", "active", 1);
    }
    return false;
}

function fillCategories(selectedCategory) {
    $.ajax({
        type: "GET",
        url: "getCategories",
        success: function (data) {
            categories = data;
            let dropdown = $("#create-category");
            helpers.buildDropdown(data, dropdown, 'Please choose a category',
                selectedCategory);
        },
        error: function (e) {
            console.log(e);
        }
    });

    let helpers = {
        buildDropdown: function (result, dropdown, emptyMessage,
                                 selectedCategory) {
            // Remove current options
            dropdown.html('');

            // Add the empty option with the empty message
            dropdown.append('<option value="" selected>' + emptyMessage + '</option>');

            // Check result isnt empty
            if (result != '') {
                // Loop through each of the results and append the option to the
                // dropdown
                $.each(result, function (k, v) {
                    if (selectedCategory == v)
                        dropdown.append('<option value="' + k + '" selected>'
                            + v + '</option>');
                    else
                        dropdown.append('<option value="' + k + '">' + v
                            + '</option>');
                });
            }
        }
    };
    return false;
}

function fillDvPreview(imageList) {
    let dvPreview = $("#dvPreview");
    dvPreview.html("");
    for (let i = 0; i < imageList.length; i++) {
        const imageResult = imageList[i];
        const img = $("<input type='image' alt='button'/>");
        img.attr("style", "height:100px;width: 100px;margin-right:10px;");
        img.attr("src", "data:image/png;base64," + imageResult.img);
        img.click({
            param1: imageResult,
            param2: imageList
        }, showImg);
        dvPreview.append(img);
    }
    return false;
}

function showImg(event) {
    let imageResult = event.data.param1;
    let imageList = event.data.param2;
    let showImg = $("#showImg");
    showImg.html("");
    $("#showImg").dialog({
        autoOpen: false,
        title: imageResult.name,
        height: 700,
        width: 1000
    });
    $(".showImage").find(".ui-widget-header").css("background", "#f78e1e");
    $(".showImage").find(".ui-widget-header").css("color", "#ffffff");
    $(".showImage").find(".ui-widget-header").css("font-weight", "bold");
    $(".showImage").find(".ui-widget-header").css("font-size", "20px");
    let imgView = $("<br /><input type='image' alt='button'/>");
    imgView.attr("style", "align:'center'");
    imgView.attr("src", "data:image/png;base64," + imageResult.img);
    let descpView = $("<p style={font-size : 18px;}>" + imageResult.description + "</p>");
    let deleteButton = $("<button><i class=\"fa fa-trash fa-2x\" aria-hidden=\"true\"></i></button><br/>");
    deleteButton.on('click', function () {
        $.confirm({
            title: 'Delete Image!',
            content: 'Do you want to delete this image?',
            confirmButton: 'Yes',
            cancelButton: 'No',
            confirm: function () {
                imageList.splice($.inArray(imageResult, imageList), 1);
                fillDvPreview(imageList);
                $("#showImg").dialog('close');
            }
        });
    });
    if (editable)
        showImg.append(deleteButton);
    showImg.append(imgView);
    showImg.append(descpView);
    $("#showImg").dialog('open');
    return false;
}

function fillComments(contentId) {
    let user;

    if (status != "NEW") {
        $.ajax({
            type: "GET",
            url: "getComments",
            data: "contentId=" + contentId,
            success: function (comments) {
                let commentsPreview = $("#oldComments");
                $("#oldComments").show();
                i = 1;
                let old = true;
                let noOfComments = comments.length;
                commentsPreview.html("");
                for (let i = 0; i < noOfComments; i++) {
                    user = comments[i][0];
                    comment = comments[i][1];
                    if (i === noOfComments - 1 && status !== "FROZEN") {
                        if (user.toLowerCase() == username.toLowerCase() && ((status == 'SAVED_EDITOR' && empRole == 'EDITOR') ||
                            (status === 'SAVED_SENIOR_EDITOR' && empRole === 'SENIOR_EDITOR') ||
                            (status === 'FROZEN' && empRole === 'REVIEWER')))
                            old = false;
                    }
                    if (old === true) {
                        user = user.charAt(0).toUpperCase() + user.slice(1);
                        let userComment = $("<div id='userCommenting' style='line-height : 0.7;'><label style='color : #6600ff; font-weight : bold;'>"
                            + user
                            + "</label></div><div id='commentText'><p style = 'margin-bottom : 5px;'>"
                            + comment + "</p></div>");
                        commentsPreview.append(userComment);
                    } else {
                        let event = {data: {param1: comment}};
                        postComment(event);
                    }
                    $('div#comments').animate({
                        scrollTop: $('div#comments').get(0).scrollHeight
                    }, 1);
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    }
    return false;
}

function saveData(empId, str) {
    let content = $('.summernote').summernote('code');
    let saveForm = new Object();
    saveForm.empId = empId;
    saveForm.category = $("#create-category").val();
    saveForm.title = $("#title-create").val();
    saveForm.imageList = imageList;
    saveForm.comment = comment;
    saveForm.source = $("#source-create").val();
    saveForm.content = content;
    saveForm.contentId = contentId;

    console.log("SAVE FORM : ", saveForm);

    switch (str) {
        case "save":
            saveForm.saveSubmit = 'SAVE';
            break;
        case "revert":
            saveForm.saveSubmit = 'REVERT';
            break;
        case "freeze":
            saveForm.saveSubmit = 'FREEZE';
            break;
        case "publish":
            saveForm.saveSubmit = 'PUBLISH';
            break;
    }
    saveForm.status = status;

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "saveOrSubmit", true);
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(JSON.stringify(saveForm));
    $.alert({
        title: 'Success!',
        animation: 'none',
        content: 'Data updated successfully.',
        confirmButton: 'Ok',
        confirm: function () {
            if (navigation == "view")
                fillData();
            else {
                startup();
                $("#tabs").tabs("option", "active", 1);
            }
        }
    });
    return false;
}

function unfreezeContent() {
    $.confirm({
        title: 'Unfreeze Confirm!',
        content: 'Are you sure you want to unfreeze this section?',
        confirm: function () {
            $.alert({
                title: 'Reason for unfreezing',
                backgroundDismiss: true,
                animation: 'none',
                content: 'Please enter a comment mentioning the reason for unfreezing content!',
                confirmButton: 'Ok'
            });
            $("#unfreeze").hide();
            $("#newComment").show();
            $("#commentText").focus();
            bind("unfreeze");
        },
        confirmButton: 'Yes',
        cancelButton: 'No'
    });
    return false;
}

function unfreezeContinue(comment) {
    if (comment != "") {
        if (empRole == "EDITOR")
            status = "SAVED_EDITOR";
        else if (empRole == "SENIOR_EDITOR")
            status = "SAVED_SENIOR_EDITOR";
        else if (empRole == "REVIEWER") {
            status = "SUBMITTED_TO_REVIEWER";
            $("#freeze").show();
        }
        $('#create-category').prop('disabled', false);
        $("#title-create,#source-create").prop('disabled', false);
        $("#new-category").show();
        $("#images").show();
        $('.note-editable').attr('contenteditable', true);
        $("#create-category,#title-create,#source-create,.note-editable").css('background', '#ffffff');
        $("#revert,#save,#submit").show();
        editable = true;
        bind("");
    }
}

function submitData(empId, str) {
    let flag = 1;
    comment = $("textarea#commentText").val();
    $("#category-validation").html("Please choose a category");
    $("#title-validation").html("Please Enter a title");
    $("#content-validation").html("Please enter some content in editor");

    $("#category-validation").hide();
    $("#title-validation").hide();
    $("#content-validation").hide();

    let isEmpty = /^\s*(?:<p>|)\s*(?:<br>|)\s*(?:<\/p>|)\s*$/i;
    let content = $('.summernote').summernote('code');

    if ($("#create-category").val() == '') {
        $("#category-validation").show();
        flag = 0;
    }
    if ($("#title-create").val() == '') {
        $("#title-validation").show();
        flag = 0;
    }

    if (isEmpty.test(content) == true) {
        $("#content-validation").show();
        flag = 0;
    }

    if (flag == 1) {
        let saveForm = new Object();
        saveForm.empId = empId;
        saveForm.category = $("#create-category").val();
        saveForm.title = $("#title-create").val();
        saveForm.imageList = imageList;
        saveForm.comment = comment;
        saveForm.source = $("#source-create").val();
        saveForm.content = content;

        switch (str) {
            case "revert":
                saveForm.saveSubmit = 'REVERT';
                break;
            case "freeze":
                saveForm.saveSubmit = 'FREEZE';
                break;
            case "publish":
                saveForm.saveSubmit = 'PUBLISH';
                break;
            case "submit":
                saveForm.saveSubmit = 'SUBMIT';
                break;
        }

        saveForm.status = status;
        saveForm.contentId = contentId;
        let xhr = new XMLHttpRequest();
        xhr.open("POST", "saveOrSubmit", true);
        xhr.setRequestHeader('Content-type', 'application/json');
        xhr.send(JSON.stringify(saveForm));
        $.alert({
            title: 'Success!',
            animation: 'none',
            content: 'Submission successful.',
            confirmButton: 'Ok',
            confirm: function () {
                if (navigation === "view")
                    fillData();
                else {
                    startup();
                    $("#tabs").tabs("option", "active", 1);
                }
            }
        });
    }
    return false;
}

function fillData() {
    unbind();
    if (contentId != '')
        unlockObject();
    navigation = "view";
    if (form == null)
        form = $("#form").detach();
    $("#view").show();
    $("#open-content").hide();
    $("#viewContent").html("");
    $.ajax({
        type: "GET",
        url: "getView",
        data: {
            'empId': empId
        },
        success: function (result) {
            $.each(result, function (key, value) {
                let statusString = (value.status.replace(/_/g, " ")).toLowerCase();
                let a = "";
                if (value.sessionId == null) {
                    a = $("<tr><td class='contents'><a>" + value.category
                        + " - " + value.title + "</a></td><td style='width: 220px;'><div class='statusString'>" + statusString
                        + "</div></td></tr>");
                    a.click({
                        param1: value.contentId,
                        param2: value.editable,
                        param3: value.status,
                        param4: value.role,
                        param5: value.category,
                        param6: value.title,
                        param7: value.content,
                        param8: value.source,
                    }, showContent);
                } else
                    a = $("<tr><td class='contents'><i class='fa fa-lock' aria-hidden='true'></i>" + value.category
                        + " - " + value.title + "</td><td style='width: 220px;'><div class='statusString'>" + statusString
                        + "</div></td></tr>");
                $("#viewContent").append(a);
            });
        },
        error: function (e) {
            console.log(e.responseText);
        }
    });
    return false;
}

function showContent(event) {
    clear();
    contentId = event.data.param1;
    editable = event.data.param2;
    status = event.data.param3;
    role = event.data.param4;
    categoryVal = event.data.param5;
    title = event.data.param6;
    content = event.data.param7;
    source = event.data.param8;
    data = "mode=view&status=" + status + "&role=" + role;
    $("#view").hide();
    $("#open-content").show();
    if (editable === true) {
        $.ajax({
            type: "GET",
            url: "lockDocument",
            data: "contentId=" + contentId,
            error: function (e) {
                console.log(e.responseText);
            }
        });
    }
    $.post("data.jsp?" + data, function (result) {
        if (empRole === "EDITOR")
            $('#open-content').html("<a id='back' style='margin-left : 15px;' onclick='fillData()'>Back</a><br/>" + result);
        else
            $('#open-content').html("<br/><a id='back' style='margin-left : 15px;' onclick='fillData()'>Back</a><br/>" + result);
        mode = false;
        getImages(contentId);
        startup_view(contentId);
    });
    return false;
}

function getImages(contentId) {
    $.ajax({
        type: "GET",
        url: "getImages",
        data: "contentId=" + contentId,
        success: function (data) {
            imageList = [];
            imageList = data;
            fillDvPreview(imageList);
        },
        error: function (e) {
            console.log(e.responseText);
        }
    });
    return false;
}

function postComment(event) {
    comment = event.data.param1;
    let unfreeze = false;
    if (comment == "unfreeze") {
        unfreeze = true;
        comment = "";
    }
    if (comment == "")
        comment = $("textarea#commentText").val();
    if (comment != "") {
        $("textarea#commentText").val(comment);
        $("#newComment").hide();
        $("#postedComment").show();
        let commentsPreview = $("#postedComment");
        commentsPreview.html("");
        let userComment = $("<div id='userCommenting' style='line-height : 0.7;'><label style='color : #404040;, font-weight : bold;'>"
            + username
            + "</label></div><div id='commentText'><p style='margin-bottom : 5px;'>"
            + comment + "</p>");
        let edit = $("<input type='button' value='Edit' class = 'Edit' style='margin-left:190px;'/></div>");
        edit.on("click", function editComment() {
            commentsPreview.html("");
            $("#newComment").show();
        });
        commentsPreview.append(userComment);
        commentsPreview.append(edit);
        if (unfreeze)
            unfreezeContinue(comment);
    }
    return false;
}


function startup_view(contentId) {
    fillCategories(categoryVal);
    $("#title-create").val(title);
    $("#source-create").val(source);
    $("#category-validation").hide();
    $("#title-validation").hide();
    $("#comment-validation").hide();
    $("#content-validation").hide();
    $("#dvPreview").html("");
    $("#postedComment").hide();
    $("textarea#commentText").val('');
    dialogs();
    $('.summernote').summernote(summernote_options);
    $('.summernote').summernote('reset');
    $('.summernote').summernote('code', content);
    fillComments(contentId);

    editable ? bind("") : disable_fields();
    return false;
}

function image_click() {
    $("#fileupload").val("");
    $("#imgName").val("");
    $("#descp").val("");
    $("#dialog-create").dialog('open');
    return false;
}

function new_category() {
    $("#newCategory").removeClass('warning');
    $("#newCategory").val("");
    $("#category-dialog").dialog('open');
    return false;
}

function upload_image() {
    $("#fileupload").removeClass('warning');
    $("#imgName").removeClass('warning');
    $("#descp").removeClass('warning');
    if ($("#fileupload").val() == ''
        || $("#imgName").val() == ''
        || $("#descp").val().trim().length == 0) {
        $.alert({
            title: 'Image name and description required',
            animation: 'none',
            backgroundDismiss: true,
            confirmButton: 'Ok',
            content: 'Please fill all fields marked in red!'
        });
        if ($("#fileupload").val() == '')
            $("#fileupload").addClass('warning');
        if ($("#imgName").val() == '')
            $("#imgName").addClass('warning');
        if ($("#descp").val().trim().length == 0)
            $("#descp").addClass('warning');
    } else {
        let ext = $('#fileupload').val().split('.').pop().toLowerCase();
        let size = document.getElementById('fileupload').files[0].size;
        if ($.inArray(ext, ['gif', 'png', 'jpg', 'jpeg']) == -1) {
            $.alert({
                title: 'Invalid File!',
                backgroundDismiss: true,
                animation: 'none',
                confirmButton: 'Ok',
                content: 'Please select image files only.'
            });
            $("#fileupload").addClass('warning');
        } else if (size > 5120000) {
            $.alert({
                title: 'Invalid File!',
                backgroundDismiss: true,
                animation: 'none',
                confirmButton: 'Ok',
                content: 'Please select image files less than 5 MB.'
            });
            $("#fileupload").addClass('warning');
        } else {
            let file = document.getElementById('fileupload').files[0];
            let imgForm = new FormData();
            imgForm.append("file", file);
            imgForm.append("imgName", $('#imgName').val());
            imgForm.append("imgDescp", $('#descp').val());

            $.ajax({
                type: "POST",
                url: "upload",
                data: imgForm,
                contentType: false,
                processData: false,
                success: function (result) {
                    let imageResult = JSON
                        && JSON.parse(JSON
                            .stringify(result))
                        || $.parseJSON(JSON
                            .stringify(result));
                    imageList.push(imageResult);
                    fillDvPreview(imageList);
                    $("#dialog-create").dialog('close');
                },
                error: function (e) {
                    console.log(JSON.stringify(e));
                }
            });
        }
    }
    return false;
}

function add_category() {
    let isCategoryExisting = false;
    let newCategory = $("#newCategory").val();
    if (newCategory === '')
        $("#category-dialog").dialog('close');
    else {
        $.each(categories, function (k, v) {
            if (newCategory.toLowerCase() === v.toLowerCase()) {
                $.alert({
                    title: 'Duplicate Category!',
                    content: 'This category already exists!',
                    animation: 'top',
                    confirmButton: 'Ok',
                    backgroundDismiss: true,
                });
                $("#newCategory").addClass(
                    'warning');
                isCategoryExisting = true;
            }
        });

        if (!isCategoryExisting) {
            $.ajax({
                type: "POST",
                url: "addCategory",
                data: {
                    'newCategory': newCategory
                },
                success: function (data) {
                    fillCategories(categoryVal);
                    $("#category-dialog").dialog('close');
                },
                error: function (e) {
                    alert("error");
                }
            });
        }
    }
    return false;
}

function disable_fields() {
    $("#newComment").hide();
    $('#create-category').prop('disabled', true);
    $("#title-create,#source-create,.note-editable").prop('disabled', true);
    $("#new-category").hide();
    $("#images").hide();
    $('.note-editable').attr('contenteditable', false);
    $("#btn-display-create").hide();
    if (status === "FROZEN") {
        $("#btn-display-create").show();
        $("#revert,#save,#submit,#freeze").hide();
    }
    $(":disabled,.note-editable").css('background', '#f2f2f2');
    return false;
}

function unlockObject() {
    $.ajax({
        type: "GET",
        url: "unlockDocument",
        data: "contentId=" + contentId,
        success: function () {
            return true;
        },
        error: function (e) {
            console.log(JSON.stringify(e));
        }
    });
}

function bind(arg) {
    unbind();
    $(document).on('click', '#images', image_click);
    $(document).on('click', '#new-category', new_category);
    $(document).on('click', '#upload', upload_image);
    $(document).on('click', '#addCategory', add_category);
    $(document).on('click', '.post', {param1: arg}, postComment);
}

function unbind(arg) {
    $(document).off('click', '#images', image_click);
    $(document).off('click', '#new-category', new_category);
    $(document).off('click', '#upload', upload_image);
    $(document).off('click', '#addCategory', add_category);
    $(document).off('click', '.post', {param1: arg}, postComment);
}


