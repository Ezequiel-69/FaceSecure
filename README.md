# 🧠 FaceSecure

FaceSecure es una aplicación Android desarrollada en **Kotlin** 
que implementa un sistema básico de **registro e inicio de sesión** 
con persistencia local mediante **Room Database**, y que posteriormente integrará 
**detección y análisis facial en tiempo real** utilizando **CameraX**, **ML Kit** 
y **TensorFlow Lite**.

---

## 📱 Características actuales (versión base)

✅ Arquitectura organizada por capas:
- `data/model` → Modelos de datos (User)
- `data/local` → Acceso a base de datos (Room + DAO)
- `data/repository` → Capa de repositorio (intermediario entre DAO y ViewModel)
- `ui/screens` → Pantallas de registro, login, inicio, etc.
- `viewmodel` → Control de la lógica de UI

✅ Persistencia local implementada con **Room**
- Entidad `User` con anotaciones `@Entity` y `@PrimaryKey`.
- Campo `faceEmbedding` (lista de floats) para almacenar la representación facial del usuario.
- `Converters.kt` para convertir listas de floats a cadenas JSON y viceversa.
- `UserDao` para insertar y consultar usuarios.
- `AppDatabase` para inicializar la base de datos y exponer `userDao()`.

✅ Dependencias configuradas:
- Room (runtime, compiler y ktx)
- Kotlin coroutines
- Jetpack Compose (para interfaz)
- AndroidX core, lifecycle y activity-compose

---

## 🔧 Technologist y librerías

| Herramienta         | Uso                                                                |
|---------------------|--------------------------------------------------------------------|
| **Kotlin**          | Lenguaje principal                                                 |
| **Jetpack Compose** | Interfaz moderna y reactiva                                        |
| **Room**            | Base de datos local con ORM                                        |
| **Coroutines**      | Ejecución asíncrona (operaciones de base de datos sin bloquear UI) |
| **CameraX**         | Acceso a la cámara en tiempo real                                  |
| **ML Kit**          | Detección de rostros en tiempo real                                |
| **TensorFlow Lite** | Generación y comparación de embeddings faciales                    |

---

## 🧪 Test actuales:

Actualmente, la app incluye un test básico (`UserDaoTest.kt`) que:

- Crea una base de datos Room en memoria.
- Inserta un usuario.
- Recupera y valida los datos almacenados.

---

## 🧰 Requisitos

- Android Studio Koala+ (o posterior)
- Gradle 8.0+
- Kotlin 1.9+
- Android SDK 29+
- Emulador Android (AVD) o dispositivo físico

---

## 🧑‍💻 Autor

**Ezequiel Aceituno Schmidt** 
**Cristian Collao Aranciba**
**ChatGPT**
Desarrolladores Android en formación — Chile 🇨🇱  
📚 Proyecto académico integrando *Room + CameraX + ML Kit + TensorFlow Lite*

---

## 🪪 Licencia

MIT License © 2025 — Puedes usar este código libremente para aprendizaje o proyectos personales.



