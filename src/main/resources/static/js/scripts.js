$(".answer-write button[type=submit]").click(addAnswer);
$(document).on('click', '.link-delete-article-answer', deleteAnswer); // 위와 달리 동적으로 생성된 html도 바인딩 가능

function addAnswer(e) {
    e.preventDefault(); // form의 action로 전송하려던 것을 막는다
    console.log("click!!");

    var queryString = $(".answer-write").serialize(); // 원래 전송하려던 녀석을 변수에 담는다.
    console.log("query : " + queryString);

    var url = $(".answer-write").attr("action");
    console.log("url : " + url);

    $.ajax({
        type: 'post',
        url: url,
        data: queryString,
        dataType: 'json',
        error: onError,
        success: onSuccess
    });

    function onError(xhr, status) {
        console.log(status);
    }

    // 이곳의 data는 ApiController(RestController)의 메서드가 반환한 answer를 변환한 json 데이터를 의미함.
    function onSuccess(data, status) {
        console.log(status);

        var answerTemplate = $("#answerTemplate").html();
        var template = answerTemplate.format(data.writer.userId, data.formattedCreateDate, data.contents, data.id, data.question.id);
        $(".qna-comment-slipp-articles").append(template);
        $(".answer-write textarea").val("");
    }
}

function deleteAnswer(e) {
    e.preventDefault();
    console.log("click!!");

    var deleteBtn = $(this); // scope마다 this가 다를 수 있으니 변수처리
    var url = $(this).attr("href");

    $.ajax({
        type: 'delete',
        url: url,
        dataType: 'json',
        error: function (xhr, status) {
            console.log(status);
        },
        success: function (data, status) {
            console.log(status);

            if(data.valid) {
                deleteBtn.closest("article").remove();
            } else {
                alert(data.errorMessage)
            }
        }
    });

}

String.prototype.format = function () {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};