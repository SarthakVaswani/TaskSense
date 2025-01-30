# TaskSense 📝

Smart task prioritization powered by DeepSeek AI

<img width="270" height ="600" alt="Screenshot 2025-01-30 at 10 36 21 PM" src="https://github.com/user-attachments/assets/07548003-5d4d-47ef-93b8-d710e7447c22" />
<img width="270" height ="600"   alt="Screenshot 2025-01-30 at 10 37 47 PM" src="https://github.com/user-attachments/assets/0beed88f-5dda-4ff1-a41d-f62140d92c11" />
<img width="270" height ="600"  alt="Screenshot 2025-01-30 at 10 37 03 PM" src="https://github.com/user-attachments/assets/faa84d05-0511-4d6b-bb74-5a610fc08df9" />


## Features ✨

- **AI-Powered Priority Suggestions**: Utilizes DeepSeek API to intelligently suggest task priorities based on descriptions
- **Clean Architecture**: Implements domain-driven design with clear separation of concerns
- **Modern UI**: Built entirely with Jetpack Compose for a fluid and responsive user experience
- **Real-time Updates**: Tasks are updated and displayed in real-time
- **Priority Filtering**: Filter tasks based on their priority levels
- **Local Persistence**: Tasks are stored locally using Room database
- **Dark/Light Theme Support**: Automatic theme switching based on system settings

## Tech Stack 🛠

- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern UI toolkit for native Android UI
- **Koin**: Dependency injection framework
- **Room**: Local database for storing tasks
- **Coroutines & Flow**: Asynchronous programming
- **DeepSeek API**: AI-powered task priority suggestions
- **Material Design 3**: Modern Android design system
- **Clean Architecture**: Domain-driven design pattern
- **MVVM**: Presentation layer architecture

## Project Structure 📁

```
app/
├── data/
│   ├── local/          # Room database, DAOs
│   └── remote/         # DeepSeek API service
├── di/                 # Koin dependency injection modules
├── domain/
│   ├── model/          # Domain models
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Business logic
└── presentation/
    ├── components/     # Reusable Compose components
    ├── screen/         # Main screens
    └── viewmodel/      # ViewModels
```

## Clean Architecture 🏗

The project follows Clean Architecture principles with three main layers:

1. **Data Layer**: Implements data sources (Room & DeepSeek API)
2. **Domain Layer**: Contains business logic and repository interfaces
3. **Presentation Layer**: Handles UI and user interactions using MVVM

## Dependency Injection with Koin 💉

Koin is used for dependency injection with the following modules:

- `appModule`: ViewModels and repositories
- `networkModule`: Retrofit,DB and API services

## DeepSeek API Integration 🤖

The app uses DeepSeek's AI capabilities to:
- Analyze task descriptions
- Suggest appropriate priority levels
- Provide intelligent task management

## Getting Started 🚀

1. Clone the repository
2. Add your DeepSeek API key in `local.properties`:
   ```
   API_KEY=your_api_key_here
   ```
3. Build and run the project

## Contributing 🤝

Feel free to submit issues, fork the repository, and create pull requests for any improvements.

## License 📄

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details. 
