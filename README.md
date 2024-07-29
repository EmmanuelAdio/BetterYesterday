# BetterYesterday: Goal Setting App

### Overview

**BetterYesterday** is an Android app designed to help users stay motivated and keep track of their goals. Whether it's finishing a book or completing online courses, BetterYesterday provides tools and features to help users manage their time and track progress effectively.

### Core Features

#### Dashboard
- **Overview:** Provides users with an overview of their progress through their milestones and goals.
- **Local Storage:** Utilizes Content Provider information.

#### Goal Setting
- **Create Goals:** Allows users to set goals and break them down into smaller milestones.
- **Local Storage:** Uses a custom Content Provider.

#### Manage Milestones for Goals
- **Track Progress:** Enables users to manage the milestones they have set and track the progress of each goal.

#### Share Your Progress
- **Android Sharesheet:** Allows users to share their progress on goals with other apps.
- **Local Storage:** Utilizes local storage for data management.

#### Save Deadlines for Goals
- **Calendar Integration:** Users can save deadlines for their goals/milestones using their personal calendar app through implicit intent.

### Extended Features

- **Long Press and Double Tap:** Provides additional options such as checking further information, deleting, or sharing the goal.
- **Notifications:** Allows users to enable daily reminders for updating their goals and milestones.
- **Android Sharesheet:** Exports goal progress to other apps using the sharesheet.

### Technical Requirements

- **Distinct Screens:** Dashboard, Goal Setting, Task/Milestone Management, Share, and Settings screens.
- **Navigation Component:** Uses NavHost and NavController for seamless navigation.
- **Intent Usage:** Implements implicit intents for calendar integration.
- **Lifecycle Management:** Ensures data and UI states persist through screen rotation.
- **Permissions:** Manages necessary permissions ethically.
- **Local Storage:** Utilizes Preference Datastores and Room database for storing information.
- **Content Provider:** Shares goal and milestone data with other apps.

## Installation

To run and use BetterYesterday, follow these steps:

### Prerequisites

- Android Studio installed on your computer.
- A device or emulator running Android 5.0 (Lollipop) or higher.

### Clone the Repository

```bash
git clone https://github.com/EmmanuelAdio/BetterYesterday.git
