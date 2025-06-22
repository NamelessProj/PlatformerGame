![Java version](https://img.shields.io/badge/Java-^23.0.2-orange?style=for-the-badge)
![Project licence](https://img.shields.io/github/license/NamelessProj/PlatformerGame?style=for-the-badge)
![Repo size](https://img.shields.io/github/repo-size/NamelessProj/PlatformerGame?style=for-the-badge)

# A Simple Platformer In Java
Platform Game is a 2D platformer built in pure Java using the Swing library.
The game features multiple levels, enemies, hazards, pickups, and a functional UI with pause, game over, and level completion screens.

## Features
- Smooth player movement and jumping
- Enemies and projectiles
- Environmental hazards (spikes, projectiles)
- Collectibles (potions for health or power-ups)
- Pause, game over and level completion overlays
- Sound and volume control buttons
- Multiple game states (menu, playing, paused)
- Custom level management system

## Controls
| __Key__ | __Action__        |
|---------|-------------------|
| `A`     | Move Left         |
| `D`     | Move Right        |
| `Space` | Jump              |
| `ESC`   | Pause / Open Menu |

</br>

| __Mouse Button__ | __Action__        |
|------------------|-------------------|
| `Left Click`     | Attack            |
| `Right Click`    | Power Attack      |


## How To Build And Run
This project is built using pure Java (Swing) — We use `TinySound` for better sound management.

The project has been structured with the Maven directory layout.

- Compile :
    ```bash
    mvn clean compile
    mvn package
    ```
- Run :
    ```bash
    mvn exec:java -Dexec.mainClass="mainWindow.Main"
    ```
Alternatively, you can open the project in an IDE like __IntelliJ IDEA__ or __Eclipse__ and run `Main.java` directly.

## Project Structure
```bash
PlatformerGame-master/
├── .gitignore
├── LICENCE
├── README.md
├── pom.xml
├── .idea/                          # IntelliJ IDEA project files
├── lib/                            # Libraries: TinySound
└── src/
    └── main/
        ├── java/
        │   ├── entities/           # Player, Enemy, Crabby, EnemyManager, etc.
        │   ├── gamestates/         # Menu, Playing, State management
        │   ├── inputs/             # Keyboard and mouse input handling
        │   ├── levels/             # Level and level manager
        │   ├── mainWindow/         # Main game loop, window, panel
        │   ├── objects/            # Game objects: potions, spikes, cannons, etc.
        │   ├── ui/                 # UI components: buttons, overlays
        │   └── utils/              # Constants, helpers, loading utilities
        └── resources/              # Game resources
            ├── images/             # Images for player, enemies, backgrounds, etc.
            │   └── lvls/           # Levels data images
            └── audio/              # Audio files: songs and effects
```