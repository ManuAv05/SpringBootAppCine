import YouTube from 'react-youtube';
import './VideoPlayer.css';

function VideoPlayer({ trailerUrl, onClose }) {
    if (!trailerUrl) return null;

    const opts = {
        height: "100%",
        width: "100%",
        playerVars: {
            autoplay: 1,
            modestbranding: 1
        }
    };

    return (
        <div className="video-overlay" onClick={onClose}>
            <div className="video-container" onClick={e => e.stopPropagation()}>
                <YouTube videoId={trailerUrl} opts={opts} className="youtube-player" />
                <button className="video-close-btn" onClick={onClose}>
                    Cerrar X
                </button>
            </div>
        </div>
    );
}

export default VideoPlayer;
