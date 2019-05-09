$(".answer-write button[type=submit]").click(addAnswer);

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

    function onError(data) {
        console.log("error!!")
        console.log(data)
    }

    // 이곳의 data는 ApiController(RestController)의 메서드가 반환한 answer를 변환한 json 데이터를 의미함.
    function onSuccess(data, status) {
        console.log("success!!")
        console.log(data)

        var answerTemplate = $("#answerTemplate").html();
        var template = answerTemplate.format(data.writer.userId, data.formattedCreateDate, data.contents, data.id, data.question.id, data.id);
        $(".qna-comment-slipp-articles").append(template);
        $(".answer-write textarea").val("");
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
}