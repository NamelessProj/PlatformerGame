![Java version](https://img.shields.io/badge/Java-^23.0.2-orange?style=for-the-badge)
![Project licence](https://img.shields.io/github/license/NamelessProj/PlatformerGame?style=for-the-badge)
![Repo size](https://img.shields.io/github/repo-size/NamelessProj/PlatformerGame?style=for-the-badge)

![Hackatime](https://hackatime-badge.hackclub.com/U091MLPEJ3D/PlatformerGame?style=for-the-badge&logo=hackclub&logoColor=white)

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

## Settings
When you start the game, the settings will be loaded. In there we store the volume, if the music is muted, if the sfx are muted and the scale to display the game.

The scale is calculated based on your screen size. So if the game is too big or too small, you can change in the file `settings.txt` the value of the scale. You can basically change other variables too, but the scale is the only one that you can't change in game.

## Saves
You can save the game whenever you want and the next time you play the game, you'll be able to continue where you were.

## Libraries
I used libraries to enhanced sound management.

### TinySound
This project uses [TinySound](https://github.com/finnkuusisto/TinySound) for audio playback. TinySound is a lightweight Java library that simplifies playing sound effects and music, making it easy to add responsive audio to games. It was chosen for its minimal dependencies, ease of integration with Java Swing, and reliable performance for real-time sound effects.

### Jorbis
This project uses [Jorbis](https://github.com/ymnk/jorbis) to enable playback of Ogg Vorbis audio files. Jorbis is a pure Java decoder for the Ogg Vorbis format, allowing the game to support high-quality compressed audio without native dependencies. It was chosen for its compatibility with Java projects and its ability to handle music tracks efficiently alongside TinySound.

### Tritonus-share
This project uses [Tritonus-share](http://www.tritonus.org/) to provide additional audio system support for Java, particularly for handling various audio formats and improving compatibility with Java Sound. Tritonus-share was chosen because it extends the capabilities of the standard Java Sound API, enabling smoother playback and broader format support, which is essential for reliable audio in games.

### Vorbisspi
This project uses [Vorbisspi](https://github.com/irgsmirx/vorbisspi) to enable Java Sound API support for Ogg Vorbis audio files. Vorbisspi acts as a service provider interface (SPI) that allows Java applications to read and play Ogg Vorbis files natively through the standard Java audio system. It was chosen to ensure seamless integration of Ogg Vorbis playback with Java's built-in audio capabilities, making it easier to manage and play compressed music and sound effects alongside other supported formats.

## How To Build And Run
This project has been structured with the Maven directory layout.

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
├── .idea/                          # IntelliJ IDEA project files
├── lib/                            # Libraries: TinySound
└── src/
    └── main/
        ├── java/
        │   ├── audio/              # Audio class related: AudioPlayer
        │   ├── effects/            # Effects: DialogueEffect
        │   ├── entities/           # Player, Enemy, Crabby, EnemyManager, etc.
        │   ├── gamesaves/          # GameSaves: writing and loading saves
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

## Special Thanks
Thanks to [Kaarin Gaming](https://github.com/KaarinGaming) for his [tutorial](https://www.youtube.com/watch?v=6_N8QZ47toY&list=PL4rzdwizLaxYmltJQRjq18a9gsSyEQQ-0).