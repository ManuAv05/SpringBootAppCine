import { useEffect, useState } from 'react';
import Banner from '../components/Banner/Banner';
import Row from '../components/Row/Row';
import { useMovies } from '../hooks/useMovies';
import { useAppContext } from '../context/AppContext';
import movieTrailer from 'movie-trailer';

function Home() {
    const { peliculas, loading } = useMovies();
    const {
        busqueda,
        miLista,
        setSelectedMovie,
        setTrailerUrl
    } = useAppContext();

    const [peliculaHero, setPeliculaHero] = useState(null);

    // Initial Hero Selection
    useEffect(() => {
        if (peliculas && peliculas.length > 0) {
            const random = Math.floor(Math.random() * peliculas.length);
            setPeliculaHero(peliculas[random]);
        }
    }, [peliculas]);

    // Derived lists
    const peliculasFiltradas = peliculas.filter(p =>
        p.titulo && p.titulo.toLowerCase().includes(busqueda.toLowerCase())
    );
    const topRated = peliculas.filter(p => p.valoracion >= 8);
    const recientes = peliculas.filter(p =>
        p.fechaEstreno && new Date(p.fechaEstreno) > new Date('2020-01-01')
    );

    // Handlers
    const handlePlay = (movie) => {
        movieTrailer(movie?.titulo || "")
            .then((url) => {
                const urlParams = new URLSearchParams(new URL(url).search);
                setTrailerUrl(urlParams.get("v"));
            })
            .catch((error) => console.log("No se encontró trailer", error));
    };

    const handleInfo = (movie) => {
        setSelectedMovie(movie);
        document.body.style.overflow = 'hidden';
    };

    if (loading) return <div style={{ color: 'white', padding: '100px' }}>Cargando...</div>;

    // Search View
    if (busqueda) {
        return (
            <div style={{ padding: '100px 20px' }}>
                <h2>Resultados: {busqueda}</h2>
                <div className="grid-container">
                    {peliculasFiltradas.map(p => (
                        <div key={p.id} className="movie-card" onClick={() => handleInfo(p)}>
                            <img
                                src={p.urlImagen || `https://picsum.photos/seed/${p.id}/300/450`}
                                alt={p.titulo}
                                className="movie-img"
                            />
                            <div className="movie-title">{p.titulo}</div>
                        </div>
                    ))}
                </div>
                <style>{`
                    .grid-container {
                        display: flex; flex-wrap: wrap; gap: 20px; justify-content: center;
                    }
                `}</style>
            </div>
        );
    }

    // Default Home View
    return (
        <div>
            <Banner movie={peliculaHero} onPlay={handlePlay} onInfo={handleInfo} />

            <Row
                title="Originales de ManuFlix"
                movies={peliculas}
                isLargeRow={true}
                onMovieClick={handleInfo}
            />

            <Row
                title="Tendencias"
                movies={topRated}
                onMovieClick={handleInfo}
            />

            {miLista.length > 0 && (
                <Row
                    title="Mi Lista"
                    movies={miLista}
                    onMovieClick={handleInfo}
                />
            )}

            <Row
                title="Novedades"
                movies={recientes}
                onMovieClick={handleInfo}
            />
        </div>
    );
}

export default Home;
