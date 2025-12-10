# 🎬 ManuFlix - Plataforma de Streaming Full Stack

**ManuFlix** es una aplicación web moderna de streaming de películas construida con una arquitectura Full Stack robusta. Combina la potencia y seguridad de **Spring Boot** en el backend con una interfaz de usuario dinámica y envolvente desarrollada en **React**.

El proyecto replica la experiencia de usuario de plataformas líderes como Netflix, ofreciendo navegación fluida, reproducción de trailers, gestión de perfiles de usuario y un sistema interactivo de reseñas.

## 🚀 Características Principales

### 👤 Gestión de Usuarios y Perfiles
*   **Selección de Perfil**: Pantalla de inicio estilo "Netflix" con selección de perfiles de usuario.
*   **Panel de Administración**: Interfaz dedicada para administradores (`/admin/users`) que permite crear, editar y eliminar usuarios.
*   **Roles y Permisos**: Sistema de seguridad basado en roles (ADMIN vs USER) que protege rutas sensibles.

### 🎥 Catálogo y Reproducción
*   **Exploración Inmersiva**: Banner principal heroico con video de fondo y transiciones suaves.
*   **Filtrado y Búsqueda**: Búsqueda en tiempo real por título y categorías.
*   **Modal de Detalles**: Vista detallada de películas con sinopsis, reparto y ficha técnica.
*   **Reproducción de Trailers**: Integración con APIs de video para reproducir trailers oficiales en un reproductor a pantalla completa.

### ⭐ Interacción Social
*   **Sistema de Reseñas**: Los usuarios pueden calificar películas (1-5 estrellas) y dejar comentarios escritos.
*   **Visualización en Tiempo Real**: Las críticas aparecen instantáneamente en la ficha de la película.

### 🎨 Diseño y UX (Premium CSS)
*   **Estética Dark Mode**: Diseño completamente oscuro con paleta de colores cinematográfica (#141414, #e50914).
*   **Glassmorphism**: Uso de efectos de desenfoque y transparecias en modales y navegación.
*   **Micro-interacciones**: Animaciones de hover en tarjetas, botones interactivos y transiciones de página suaves.

---

## 🛠️ Stack Tecnológico

### Backend (API REST)
*   **Java 21**: Aprovechando las últimas características del lenguaje.
*   **Spring Boot 3**: Framework principal para la creación de servicios RESTful.
*   **Spring Data JPA**: Abstracción para la persistencia de datos.
*   **H2 Database**: Base de datos en memoria para desarrollo rápido (fácilmente migratable a MySQL/PostgreSQL).
*   **Lombok**: Para reducir el código repetitivo (Boilerplate).

### Frontend (SPA)
*   **React 18**: Librería de UI para componentes interactivos.
*   **Vite**: Build tool de próxima generación para un desarrollo ultrarrápido.
*   **React Router DOM**: Gestión de rutas y navegación SPA.
*   **Axios**: Cliente HTTP para la comunicación con el backend.
*   **CSS3 Moderno**: Variables CSS, Grid, Flexbox y animaciones (sin dependencias de frameworks pesados).

---

## 📦 Instalación y Despliegue

### Requisitos Previos
*   Java JDK 21+
*   Node.js 18+
*   Maven (opcional, wrapper incluido)

### 1. Configuración del Backend

```bash
# Navegar al directorio raíz
cd SpringBootAppCine

# Ejecutar la aplicación (Windows)
./mvnw spring-boot:run
```

El servidor iniciará en `http://localhost:8080`.

> **Nota**: Al iniciar, el sistema precargará automáticamente un conjunto de datos de prueba (películas, directores y usuario admin) gracias a la clase `DataLoader`.

### 2. Configuración del Frontend

```bash
# Navegar al directorio del frontend
cd frontend

# Instalar dependencias
npm install

# Iniciar servidor de desarrollo
npm run dev
```

La aplicación estará disponible en `http://localhost:5173`.

---

## 🔑 Credenciales de Acceso

Para acceder al Panel de Administración y probar todas las funcionalidades:

*   **Usuario**: `admin`
*   **Contraseña**: `1234`

---

## 📂 Estructura del Proyecto

```
ManuFlix/
├── src/main/java/com/dam2/Practica1  # Código Fuente Backend
│   ├── config/       # Configuraciones (CORS, DataLoader)
│   ├── controller/   # Controladores REST
│   ├── domain/       # Entidades JPA (Pelicula, Usuario, Critica)
│   ├── dto/          # Data Transfer Objects
│   ├── repository/   # Interfaces de acceso a datos
│   └── service/      # Lógica de negocio
└── frontend/         # Código Fuente Frontend
    ├── public/       # Assets estáticos
    └── src/
        ├── components/ # Componentes reutilizables (Modal, Navbar, Banner)
        ├── context/    # Estado global (AppContext)
        ├── hooks/      # Custom Hooks
        └── pages/      # Vistas principales (Home, Catalog, Admin)
```

## ✨ Autor

Desarrollado como parte de una práctica avanzada de desarrollo Full Stack.
**ManuFlix** demuestra la capacidad de integrar sistemas complejos con un acabado visual profesional.
