var stompClient = null;
//连接服务器的函数
function connect() {
    var socket = new SockJS(path+'/webSckServer.do');
    stompClient = Stomp.over(socket);
    stompClient.connect({},function (frame) {
        stompClient.subscribe(path+'/topicChat/allMsg.do', function (chat) {
           var json = JSON.parse(message.body);
        });
        stompClient.subscribe(path+'/userChat/'+userCode+'.do', function (chat) {
            var json = JSON.parse(message.body);
         });
    }, function () {
        connect();
    });
}

//发送聊天信息
function sendName() {
    var input = $('#chat_input');
    var inputValue = input.val();
    input.val("");
    stompClient.send("/userChat/"+userCode+'.do', {}, JSON.stringify({
        'name': encodeURIComponent(name),
        'content': encodeURIComponent(inputValue),
        'userCode': userCode
    }));
}

//显示聊天信息
function showChat(message) {
    var response = document.getElementById('chat_content');
    response.value += decodeURIComponent(message.name) + ':' + decodeURIComponent(message.content) + '\n';
}

$(function () {
    connect();
});