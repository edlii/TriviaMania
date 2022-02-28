# My Personal Project

## TriviaMania

[comment]: <> (A *bulleted* list:)

[comment]: <> (- item 1)

[comment]: <> (- item 2)

[comment]: <> (- item 3)

[comment]: <> (An example of text with **bold** and *italic* fonts.  Note that the IntelliJ markdown previewer doesn't seem to render )

[comment]: <> (the bold and italic fonts correctly but they will appear correctly on GitHub.)

TriviaMania is a trivia game that is built on java. It will be a game with multiple categories each filled with
different types of questions. Settings will be customizable according to everyone's personal preferences. Everyone can 
play trivia, it is meant to be a simple escape from reality and something relaxing to do as a break. 

This project generally interests me as I am a big fan of trivia. The ones I generally find online are too simple so 
seeing this project as an opportunity for me to develop one, I hope to make the game more enjoyable for myself
and perhaps for a starting point for a future project of mine.


## User Stories

- As a user, I want to be able to choose the category of questions I want to play
- As a user, I want to be able to choose the question difficulties
- As a user, I want to be able to play the game in a multiple-choice format
- As a user, I want to be able to see if I got the correct answer after I play
- As a user, I want to see the correct answer if I got the answer wrong
- As a user, I want to add my points to the total points I earn per game
- As a user, I want to be able to add my own questions to the question bank
- As a user, I want to be able to save my user-created questions and incorrect answers
- As a user, I want to load and see the questions that I made
- As a user, I want to be able to remove my own questions
- As a user, I want to see the top 5 high scores of people who played
- As a user, I want to be able to have options to play the game, add new questions, and view the all-time high scores.
- As a user, I want to have a dark mode option


## Phase 4: Task 2

First add two questions called "SampleQuestion" and "SampleQuestion2", then removing the first, then starting a new game.

The events are logged as follows:

- SampleQuestion has been added to the question bank.
- Added its incorrect answers to the incorrect answers bank.
- SampleQuestion2 has been added to the question bank.
- Added its incorrect answers to the incorrect answers bank.
- User question has been removed.
- The question SampleQuestion2 has been loaded.
- Incorrect answers #1 has been loaded


## Phase 4: Task 3

We can see in the UML diagram that there is a lot of coupling between classes like question and JsonReader and JsonWriter.
To combat repetition, what I would do is make some fields static so that it can be shared throughout the entire project
without the need to redeclare and re-instantiate. Some GUI pages have duplicate methods (ex. initBackBtn()) which is a 
commonality shared between each page, so I would've introduced a hierarchy for the GUI pages (abstract GUI page class) to 
prevent the duplication. In addition, JsonReader and JsonWriter are very popular for my classes and have multiplicities > 1 most of the time.
I would consider refactoring my JSON files into one big "AllData" file to make everything in one place reducing the need
to have multiple readers and writers for different source locations.
