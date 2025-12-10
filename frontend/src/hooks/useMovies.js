import { useState, useEffect } from 'react';
import axios from 'axios';

export function useMovies() {
    const [peliculas, setPeliculas] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function fetchData() {
            try {
                // Adjust URL if needed
                const req = await axios.get('http://localhost:8081/api/peliculas');
                setPeliculas(req.data);
            } catch (err) {
                console.error("Error fetching movies:", err);
                setError(err);
            } finally {
                setLoading(false);
            }
        }
        fetchData();
    }, []);

    return { peliculas, loading, error };
}
