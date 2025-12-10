import { useMovies } from '../hooks/useMovies';
import { useAppContext } from '../context/AppContext';
import './Catalog.css';

function Catalog() {
    const { peliculas } = useMovies();
    const { setSelectedMovie } = useAppContext();

    const handleMovieClick = (movie) => {
        setSelectedMovie(movie);
        document.body.style.overflow = 'hidden';
    };

    const getImage = (peli) => {
        if (!peli) return "https://via.placeholder.com/300x450?text=No+Data";
        if (peli.urlImagen && peli.urlImagen.startsWith("http")) return peli.urlImagen;
        return `https://picsum.photos/seed/${peli.id}/300/450`;
    };

    return (
        <div className="catalog-page">
            <h2 className="catalog-title">Catálogo Completo</h2>
            <div className="catalog-grid">
                {peliculas.map(p => (
                    <div key={p.id} className="movie-card catalog-card" onClick={() => handleMovieClick(p)}>
                        <img
                            src={getImage(p)}
                            alt={p.titulo}
                            className="movie-img"
                        />
                        <div className="movie-title">{p.titulo}</div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Catalog;
