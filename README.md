# 🧠 FaceSecure — Registro y autenticación facial en Android

**FaceSecure** es una aplicación Android desarrollada en **Kotlin** con **Jetpack Compose**, que combina autenticación facial mediante **ML Kit**, persistencia local con **Room**, y manejo de cámara con **CameraX**.
Su propósito es ofrecer un flujo completo de registro y login de usuarios con reconocimiento facial embebido.

---

## 🚀 Características principales

✅ **Registro de usuario completo** (correo, contraseña y rostro)
✅ **Captura facial con CameraX**
✅ **Extracción de embeddings faciales** usando TensorFlow Lite
✅ **Comparación de rostros** con `FaceComparator`
✅ **Base de datos local Room** para almacenar usuarios
✅ **Flujo de navegación con Jetpack Navigation Compose**
✅ **Arquitectura MVVM** con `ViewModel`, `Repository` y `RoomDatabase`
✅ **Pruebas instrumentadas** para analizar cámara y flujo de registro
✅ **Diseño limpio y moderno** con Material3

---

## 📱 Flujo general de la aplicación

1. **Inicio (`StartScreen`)**
   Pantalla de bienvenida que permite acceder al registro o login.

2. **Registro (`RegisterScreen`)**
   El usuario ingresa su correo y contraseña.
   Al continuar, se reproduce un sonido y navega a la captura facial.

3. **Reconocimiento facial (`FacialRecognitionScreen`)**
   Activa la cámara frontal y usa **ML Kit Face Detection** para detectar el rostro.
   Luego genera un **embedding facial (vector de 512 floats)** con el modelo `facenet.tflite`.

4. **Almacenamiento**
   El `RegisterViewModel` combina los datos y los guarda en Room como un objeto `User`:

   ```kotlin
   User(
       email = "usuario@correo.com",
       password = "123456",
       faceEmbedding = [...512 floats...]
   )
   ```

5. **Inicio de sesión facial (próxima fase)**
   Compara el embedding actual con los guardados para validar identidad.

---

## 🏗️ Arquitectura del proyecto

**MVVM + Clean Architecture**

```
app/
 ├── data/
 │   ├── local/             # Room Database, DAO y Converters
 │   ├── model/             # Modelos de datos (User)
 │   └── repository/        # Lógica de acceso a datos
 ├── camera/                # Controladores de cámara y análisis facial
 ├── ml/                    # Modelos de machine learning (TFLite, comparador)
 ├── ui/
 │   ├── screens/           # Pantallas Compose (Start, Register, FacialRecognition, etc.)
 │   └── theme/             # Colores y estilos
 ├── viewmodel/             # ViewModels de Login, Register y User
 ├── test/                  # Actividades de prueba (CameraManager, FaceAnalyzer, etc.)
 └── MainActivity.kt        # Punto de entrada, configuración de navegación
```

---

## 🧩 Tecnologías utilizadas

| Componente             | Librería                      | Versión aproximada |
| ---------------------- | ----------------------------- | ------------------ |
| 🧠 Machine Learning    | TensorFlow Lite               | 2.14.0             |
| 🤖 Face Detection      | ML Kit                        | 16.1.6             |
| 📸 Cámara              | CameraX                       | 1.3.0              |
| 💾 Base de datos local | Room                          | 2.6.1              |
| 🎨 Interfaz moderna    | Jetpack Compose + Material3   | BOM                |
| 🧭 Navegación          | Navigation Compose            | 2.7+               |
| 🧱 Arquitectura        | MVVM + ViewModel + Coroutines | —                  |
| 🧪 Pruebas             | JUnit + AndroidX Test Rules   | 1.5.0              |

---

## ⚙️ Instalación y configuración

1. **Clonar el repositorio**

   ```bash
   git clone https://github.com/tuusuario/facesecure.git
   cd facesecure
   ```

2. **Abrir en Android Studio**

   * Usa **Android Studio Koala (o superior)**.
   * Selecciona “Open existing project”.

3. **Configurar el entorno**

   * SDK mínimo: 29
   * SDK objetivo: 36
   * Activa permisos de cámara en `AndroidManifest.xml`:

     ```xml
     <uses-permission android:name="android.permission.CAMERA" />
     <uses-permission android:name="android.permission.INTERNET" />
     ```

4. **Ejecutar el proyecto**

   * Usa la configuración **“FaceSecure (app)”**.
   * Emulador o dispositivo físico con cámara frontal.

---

## 🧠 Flujo de registro facial (detalle técnico)

1. **CameraManager.kt**
   Inicia la cámara frontal y procesa cada frame con `FaceAnalyzer`.

2. **FaceAnalyzer.kt**

   * Detecta el rostro con ML Kit.
   * Convierte el frame a `Bitmap`.
   * Recorta el rostro y obtiene el embedding facial con `FaceEmbeddingExtractor`.

3. **RegisterViewModel.kt**

   * Combina correo + contraseña + embedding.
   * Inserta el `User` en `Room`.
   * Muestra el estado del registro con `StateFlow`.

4. **HomeScreen.kt**
   Pantalla final tras un registro exitoso.

---

## 🧪 Pruebas del flujo facial

Puedes usar la actividad de prueba:

```
com.example.facesecure.test.TestRegisterFlowActivity
```

O el test de cámara individual:

```
com.example.facesecure.test.TestCameraManagerActivity
```

En ambos casos, puedes seguir el log en **Logcat** con el filtro:

```
FACIAL_SCREEN | REGISTER_FLOW | CAMERA_MANAGER
```

Esto mostrará todos los pasos del flujo (detección, embeddings, guardado, errores).

---

## 📂 Base de datos local (Room)

* **Nombre:** `face_secure_database`
* **Entidad:** `User`
* **Campos:**

  ```kotlin
  data class User(
      @PrimaryKey(autoGenerate = true) val id: Int = 0,
      val email: String,
      val password: String,
      val faceEmbedding: List<Float>
  )
  ```

Puedes inspeccionarla desde **Device File Explorer → data/data/com.example.facesecure/databases/**
Archivo: `face_secure_database`

---

## 🧱 Próximas mejoras

🔸 Implementar autenticación facial (comparación de embeddings con `FaceComparator`)
🔸 Encriptar embeddings y contraseñas
🔸 Integrar almacenamiento remoto (Firebase / API)
🔸 Agregar pantalla de perfil y cierre de sesión
🔸 Optimizar rendimiento del detector en tiempo real

---

## 👩‍💻 Autoría

**Desarrollado por:** Ezequiel Aceituno y Cristian Collao
**Proyecto académico y experimental con reconocimiento facial local.**
