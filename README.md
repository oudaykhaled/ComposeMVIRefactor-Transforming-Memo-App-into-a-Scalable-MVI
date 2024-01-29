
# Refactoring a Jetpack Compose Project to MVI Architecture

## Introduction

This README is part of a sample Android application project demonstrating how to refactor a poorly structured Jetpack Compose project into a more robust, scalable, and maintainable architecture using the Model-View-Intent (MVI) pattern. This project aims to provide a practical guide for implementing MVI with Jetpack Compose in Android development.

## Project Overview

The project consists of a simple memo application built with Jetpack Compose. Initially, the app is structured with basic and unorganized code, which we will refactor to implement the MVI architecture.

## Prerequisites

-   Android Studio Arctic Fox or later
-   Knowledge of Kotlin and Jetpack Compose
-   Basic understanding of MVI architecture

## Setup

1.  Clone the repository to your local machine.
2.  Open the project in Android Studio.
3.  Sync the project with Gradle files to resolve dependencies.

## Project Structure


The project is structured into a series of branches, each representing a step in the refactoring process to implement the MVI architecture. Here's an overview of each branch:

-   `main`: This is the default branch containing the initial state of the project, which will serve as the baseline for our refactoring.
    
-   `01-create-e2e-test`: This branch focuses on creating end-to-end tests for the existing application. It ensures that we have a solid testing foundation before starting the refactoring process.
    
-   `02-move-to-mvi`: Here, we start the transition towards MVI by restructuring the codebase to align with the Model-View-Intent pattern. This branch lays the groundwork for the MVI architecture.
    
-   `03-remove-viewmodel-from-screen`: The purpose of this branch is to decouple the ViewModel from the UI screens, a key step in adhering to the MVI principles which dictate that the ViewModel should not be directly tied to the View.
    
-   `04-make-memo-Immutable`: In this branch, we refactor the memo data class to be immutable. This is an essential part of MVI, as immutability leads to easier state management and fewer bugs.
    

Each branch is a step toward transforming the project into a clean, maintainable architecture. It is recommended to review each branch in sequence to understand the progression and changes made at each step.

## Refactoring Steps

### Step 1: Understanding the Existing Code

-   Analyze the existing project structure and identify areas of improvement.
-   Locate the UI components, business logic, and data handling parts.

### Step 2: Implementing the MVI Architecture

-   **Model**: Define state and intent classes to represent the UI state and user actions.
-   **View**: Modify Composables to represent the UI. The view should only render the UI based on the state it receives and send user intents to the ViewModel.
-   **Intent**: Create sealed classes to represent all possible user actions.
-   **ViewModel**: Refactor existing ViewModels to handle intents, update states, and interact with the Model layer.

### Step 3: Integrating Components

-   Connect the View, ViewModel, and Model layers following the MVI pattern.
-   Use State and Event flows to communicate between these layers.

### Step 4: Testing and Debugging

-   Write unit tests for ViewModel and other logic components.
-   Debug the application to ensure that the MVI architecture works as expected.

## Contributing

Contributions to this project are welcome, especially in the areas of enhancing the MVI implementation, adding new features to the memo application, and improving the documentation.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
