const signalingChannel = new WebSocket('wss://59.29.102.45:8443/socket');
const configuration = {'iceServers': [{'urls': 'stun:stun.l.google.com:19302'}]}
const localVideo = document.getElementById("localVideo");
const remoteVideo = document.getElementById("remoteVideo");

signalingChannel.onopen = function () {
    console.log("Connected to the signaling server");
}
function send(message) {
    signalingChannel.send(JSON.stringify(message));
}
//calling side
async function makeCall() {
    const peerConnection = new RTCPeerConnection(configuration);
    // allow video and audio
    // navigator.mediaDevices.getUserMedia({video:true, audio:false}).
    // then(function(stream) {
    //     localVideo.srcObject = stream; // 현재 내 웹캠을 localVideo에 연결
    //     peerConnection.addTrack(stream.getVideoTracks()[0], stream);
    //     // peerConnection.addTrack(stream.getAudioTracks()[0], stream);
    // }).catch(function(err) { console.log(err) });

    signalingChannel.addEventListener('message', async message => {
        if (message.answer) {
            const remoteDesc = new RTCSessionDescription(message.answer);
            await peerConnection.setRemoteDescription(remoteDesc);
        }
    });

    const offer = await peerConnection.createOffer();
    console.log('create offer');
    await peerConnection.setLocalDescription(offer);
    console.log('setLocalDescription -- offer');
    send({'offer': offer});
    console.log('send offer')
}

//Receiving call
const peerConnection = new RTCPeerConnection(configuration);
signalingChannel.addEventListener('message', async message => {
    if (message.offer) {
        console.log('receive offer');
        peerConnection.setRemoteDescription(new RTCSessionDescription(message.offer));
        console.log('save offer');
        const answer = await peerConnection.createAnswer();
        console.log('create answer')
        await peerConnection.setLocalDescription(answer);
        console.log('setLocalDescription -- answer');
        send({'answer': answer});
        console.log('save answer');
    }
});
// peerConnection.addEventListener('track', async (event) => {
//     const [remoteStream] = event.streams;
//     remoteVideo.srcObject = remoteStream;
// });


// Listen for local ICE candidates on the local RTCPeerConnection
peerConnection.addEventListener('icecandidate', event => {
    if (event.candidate) {
        console.log('Listen for ocal ICE candidates on the local RTCPeerConnection');
        send({'new-ice-candidate': event.candidate});
    }
});

// Listen for remote ICE candidates and add them to the local RTCPeerConnection
signalingChannel.addEventListener('message', async message => {
    if (message.iceCandidate) {
        try {
            console.log('Listen for remote ICE candidates and add them to the local RTCPeerConnection');
            await peerConnection.addIceCandidate(message.iceCandidate);
        } catch (e) {
            console.error('Error adding received ice candidate', e);
        }
    }
});

// Listen for connectionstatechange on the local RTCPeerConnection
peerConnection.addEventListener('connectionstatechange', event => {
    if (peerConnection.connectionState === 'connected') {
        console.log('peers connected');
    }
});