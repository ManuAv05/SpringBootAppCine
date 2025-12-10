import { FaPlay, FaInfoCircle } from 'react-icons/fa';
import './Banner.css';

function Banner({ movie, onPlay, onInfo }) {
    if (!movie) return null;

    const getBackdrop = (peli) => {
        if (!peli) return "https://via.placeholder.com/1920x1080?text=No+Image";
        if (peli.urlImagen && peli.urlImagen.startsWith("http")) return peli.urlImagen;
        // Fallback mainly for demo
        return `https://picsum.photos/seed/${peli.id}/1920/1080`;
    };

    return (
        <header
            className="banner"
            style={{
                backgroundImage: `url("${getBackdrop(movie)}")`
            }}
        >
            <div className="banner__contents">
                <h1 className="banner__title">
                    {movie.titulo}
                </h1>
                <div className="banner__buttons">
                    <button className="banner__button play" onClick={() => onPlay(movie)}>
                        <FaPlay /> Reproducir
                    </button>
                    <button className="banner__button info" onClick={() => onInfo(movie)}>
                        <FaInfoCircle /> Más información
                    </button>
                </div>
                <h1 className="banner__description">
                    {movie.sinopsis ? (
                        movie.sinopsis.length > 150
                            ? movie.sinopsis.substring(0, 150) + "..."
                            : movie.sinopsis
                    ) : "Descubre esta increíble película en ManuFlix."}
                </h1>
            </div>
            <div className="banner--fadeBottom" />
        </header>
    );
}

export default Banner;
