# Flag-Guesser 
Currently deployed at https://flagguesser.pages.dev/daily

**Please note:** As this project is being hosted locally on my own hardware, it may be subject to unexpected downtime.

## The Game
Flag-Guesser is a Wordle-like daily game where the user is presented with a random country flag, and must guess which country that flag belongs to. The user has a drop down list of all the possible countries and will have 4 guesses to correctly identify the country. After each incorrect guess, the user is given a hint to help narrow down their selection.

There are two gamemodes in Flag-Guesser. The default when loading the page is the "daily" mode. In this gamemode, every user is presented with the same daily flag. After guessing correctly, or using all 4 guesses, the game ends and the user must wait until the next daily flag is available to play again. Playing this mode will count directly towards the user's stats that are saved in the browser and can be viewed in the stats page. The second gamemode is "practice".

In this gamemode, the user can play the game as much as they want. This functions essentially the same as the daily mode, however, once the user has guessed correctly or exhausted all their guesses, the site will load a new flag for the user to continue playing. These flags are presented in a random order and will not be the same between users. Playing the practice mode will have no effect on a user's stats.

## The Tech
All of the country information used for the game is stored in a PostgreSQL database. This database contains a single table with columns for country name, capital city, continent, international country code, and a URL for an image of the flag.

The back end of the game is a Java/Spring Boot application that uses JDBC to query the database for country information depending on the gamemode the user is currently playing. There is logic to randomly select a row from the databse every 24 hours to be used as the daily flag. This information is cached in the server application so the database does not need to be queried every time a player plays the daily gamemode.

The database information retrieved by the back-end is then served to a Vue.js based front-end. The daily information is stored by the client to avoid unnecessary requests to the API when switching between pages. All of the game logic is handled in the front-end. This is where the user's guess is compared to the information from the API to determine correct and incorrect answers and how to progress the game. The game also keeps track of the user's gamestate if they were to close and reopen the page. There is also a check for if the player has already completed the game for that day. The way this is handled is by having the back-end application generate a unicode timestamp every time the daily flag information is picked from the database. This then gets sent to the client when they load the game. If the player has played before, the browser's local storage will have saved a similar timestamp marking the last time they completed the game. These two timestamps are compared to determine if the daily information was changed after the last time the user played the game. If it was not updated, then the user is shown a message letting them know they have already completed the game for the day.

The stats page uses the charts.js library to visually display the information that gets saved to local storage. This information tracks number of games played, number of games won, current win streak, longest win streak, and how many times the player has won based on the number of guesses it took to win.

## Hosting & CI/CD
For ease of development/deployment, this project uses GitHub actions to autmatically build and push a Docker container whenever a change is pushed to the main branch of this repository. From there, re-running Docker compose allows for quick modification and redeployment for both the environment and underlying code.

The web front end of Flag Guesser is hosted through Cloudflare Pages at https://flagguesser.pages.dev/daily. The Spring Boot server application and PostgreSQL database are locally hosted as Docker containers, composed with 3rd party containers Nginx, and Certbot. Using Docker compose also alows for injecting environment variables at runtime meaning nothing sensitive is stored in the GitHub repository. Nginx acts as a proxy to route traffic to the Spring Boot server container as well as using an SSL certificate obtained from the Certbot container using Letsencrypt. This configuration, combined with front end hosting from Cloudflare, allows for end-to-end SSL ecnryption for this application.
