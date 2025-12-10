import { useRef } from 'react';
import './Row.css';
import { FaChevronLeft, FaChevronRight } from 'react-icons/fa';

function Row({ title, movies, isLargeRow, onMovieClick }) {
    const rowRef = useRef(null);

    const getImage = (peli) => {
        if (!peli) return "https://via.placeholder.com/300x450?text=No+Data";
        if (peli.urlImagen && peli.urlImagen.startsWith("http")) return peli.urlImagen;
        return `https://picsum.photos/seed/${peli.id}/300/450`;
    };

    const handleScroll = (direction) => {
        if (rowRef.current) {
            const { scrollLeft, clientWidth } = rowRef.current;
            const scrollTo = direction === 'left'
                ? scrollLeft - clientWidth / 2
                : scrollLeft + clientWidth / 2;

            rowRef.current.scrollTo({ left: scrollTo, behavior: 'smooth' });
        }
    };

    // Optional: Mouse wheel horizontal scroll
    const handleWheel = (e) => {
        if (e.deltaY === 0) return;
        // e.preventDefault(); // Warning: Passive event listener
        if (rowRef.current) {
            rowRef.current.scrollLeft += e.deltaY;
        }
    };

    if (!movies || movies.length === 0) return null;

    return (
        <div className="row">
            <h2>{title}</h2>
            <div className="row__container group">
                <div className="slider-arrow left" onClick={() => handleScroll('left')}>
                    <FaChevronLeft />
                </div>

                <div className="row__posters" ref={rowRef} onWheel={handleWheel}>
                    {movies.map((movie) => (
                        <div
                            key={movie.id}
                            className={`movie-card ${isLargeRow ? "large" : ""}`}
                            onClick={() => onMovieClick(movie)}
                        >
                            <img
                                className="movie-img"
                                src={getImage(movie)}
                                alt={movie.titulo}
                                loading="lazy"
                            />
                            {/* Overlay Title on Hover could go here via CSS or extra div */}
                            <div className="movie-title-overlay">{movie.titulo}</div>
                        </div>
                    ))}
                </div>

                <div className="slider-arrow right" onClick={() => handleScroll('right')}>
                    <FaChevronRight />
                </div>
            </div>
        </div>
    );
}

export default Row;
