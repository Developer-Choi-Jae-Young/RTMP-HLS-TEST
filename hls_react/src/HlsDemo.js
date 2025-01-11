import React, { useEffect, useRef } from 'react';
import Hls from 'hls.js';

const HlsDemo = () => {
    const videoRef = useRef(null);
    const url = 'http://localhost:8081/test/index.m3u8';

    useEffect(() => {
        const video = videoRef.current;

        if (Hls.isSupported()) {
            const hls = new Hls({ debug: true });
            hls.loadSource(url);

            // When the media is attached, start playing
            hls.attachMedia(video);
            hls.on(Hls.Events.MEDIA_ATTACHED, () => {
                video.muted = true;
                video.play();
            });

            return () => {
                // Cleanup hls instance when component unmounts
                hls.destroy();
            };
        } else if (video.canPlayType('application/vnd.apple.mpegurl')) {
            // Fallback for browsers with native HLS support (Safari)
            video.src = url;
            video.addEventListener('canplay', () => {
                video.play();
            });

            return () => {
                video.removeEventListener('canplay', () => {
                    video.play();
                });
            };
        }
    }, [url]);

    return (
        <div style={{ textAlign: 'center' }}>
            <h1>Hls Demo</h1>
            <video ref={videoRef} height="600" controls />
        </div>
    );
};

export default HlsDemo;
