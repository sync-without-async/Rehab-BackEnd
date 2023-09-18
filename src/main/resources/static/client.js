//connecting to our signaling server
// let conn = new WebSocket('ws://localhost:8080/socket');
let tempUUID = '42c8a8e3-7d3c-4581-9dea-4bbdb494806a';
let conn = new WebSocket('wss://localhost:8443/socket/' + tempUUID);

conn.onopen = function() {
    console.log("Connected to the signaling server");
    initialize();
};

conn.onmessage = function(msg) {
    let content = JSON.parse(msg.data);
    let data = content.data;
    switch (content.event) {
        // when somebody wants to call us
        case "offer":
            handleOffer(data);
            break;
        case "answer":
            handleAnswer(data);
            break;
        // when a remote peer sends an ice candidate to us
        case "candidate":
            handleCandidate(data);
            break;
        default:
            break;
    }
};

function send(message) {
    conn.send(JSON.stringify(message));
}

let peerConnection;
let localVideo = document.getElementById("localVideo");
let remoteVideo = document.getElementById("remoteVideo");
let input = document.getElementById("messageInput");

function initialize() {
    let configuration = {
        "iceServers" : [ {
            "url" : "stun:stun.l.google.com:19302"
        }
        ]
    };
    peerConnection = new RTCPeerConnection(configuration);

    // allow video and audio
    const constraints = {
        video: true,
        audio : false
    };

    navigator.mediaDevices.getUserMedia(constraints).
    then(function(stream) {
        localVideo.srcObject = stream; // 현재 내 웹캠을 localVideo에 연결
        peerConnection.addTrack(stream.getVideoTracks()[0], stream);
        // peerConnection.addTrack(stream.getAudioTracks()[0], stream);
    }).catch(function(err) { alert("비디오연결 안됨") });

    peerConnection.onicecandidate = function(event) {
        if (event.candidate) {
            send({
                event : "candidate",
                data : event.candidate
            });
            console.log('send ice candidate');
        }
    };

    peerConnection.ondatachannel = function (event) {
        console.log('peerConnection datachannel');
        dataChannel = event.channel;
    };

    peerConnection.ontrack = function (event) {
        // 원격 비디오 스트림을 remoteVideos 객체를 사용하여 표시
        remoteVideo.srcObject = event.streams[0];
        remoteVideo.autoplay = true;
    }
}

function createOffer() {
    peerConnection.createOffer(function (offer) {
        console.log('1. createOffer');
        peerConnection.setLocalDescription(offer).then(function () {
            console.log('2. saved offer');
            send({
                event: "offer",
                data: offer,
            });
            console.log('3. send offer');
        });
    }, function (error) {
        alert("Error creating an offer");
    });
}

function handleOffer(offer) {
        peerConnection.setRemoteDescription(new RTCSessionDescription(offer)).then(function () {
            console.log('4. Remote : save offer');
            peerConnection.createAnswer().then(function (answer) {
                console.log('5. Remote : create Answer');
                peerConnection.setLocalDescription(answer).then(function () {
                    console.log('6. Remote : save answer');
                    send({
                        event: "answer",
                        data: answer,
                    });
                    console.log('7. Remote : send answer');
                });
            });
    });
}

function handleCandidate(candidate) {
    console.log("receive candidate");
    peerConnection.addIceCandidate(new RTCIceCandidate(candidate))
        .then(() => {
            console.log("add candidate");
        })
        .catch((error) => {
            console.error("Error adding ICE candidate:", error);
        });
};

function handleAnswer(answer) {
    console.log('8. Local : receive answer');
    peerConnection.setRemoteDescription(new RTCSessionDescription(answer))
        .then(() => {
            console.log("9. Local : save answer");
        })
        .catch((error) => {
            console.error("Error setting remote description:", error);
        });
};