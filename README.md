# Gallery App
<a href="https://drive.google.com/uc?export=view&id=1SCtccVf0J7BC5kw1OZZ_LZOBmVBsHKIq"><img src="https://drive.google.com/uc?export=view&id=1SCtccVf0J7BC5kw1OZZ_LZOBmVBsHKIq" style="width: 250px; height: 250px max-width: 50%; height: auto"/>
<p>The Gallery App is a simple Android application that allows users to browse and view photos and thumbnails of videos stored on their device.</p>

## Features

- Display a grid of photos and thumbnails of videos.
- Utilizes the MVVM architectural pattern for structured code.
- Provides a responsive and user-friendly interface for media browsing.
- Supporst landscape mode.

## Libraries & Technologies used

- Kotlin
- LiveData
- DataBinding
- Navigation Component
- Kotlin Coroutines
- MediaStore

## Home Screen when user don't grant permission:

<a href="https://drive.google.com/uc?export=view&id=1NCPSsFKZ8T9GviRtJjoaGOgkC6D2JNV_"><img src="https://drive.google.com/uc?export=view&id=1NCPSsFKZ8T9GviRtJjoaGOgkC6D2JNV_" style="width: 250px; height: 250px max-width: 50%; height: auto"/>
<a href="https://drive.google.com/uc?export=view&id=1N9vDgmZODtQp_C0CD7PRNgt2gGLxARhQ"><img src="https://drive.google.com/uc?export=view&id=1N9vDgmZODtQp_C0CD7PRNgt2gGLxARhQ" style="width: 250px; height: 250px max-width: 50%; height: auto"/>

## Home Screen when permission granted:

# Portrait
<div></div>
<a href="https://drive.google.com/uc?export=view&id=1MftQ_vt6SYMOfGxlG2njUmXn9Tawz7JL"><img src="https://drive.google.com/uc?export=view&id=1MftQ_vt6SYMOfGxlG2njUmXn9Tawz7JL" style="width: 250px; height: 250px max-width: 50%; height: auto" />
<a href="https://drive.google.com/uc?export=view&id=1MwswFK6pGHMMHTePtQtaIXU2FQxM4Gal"><img src="https://drive.google.com/uc?export=view&id=1MwswFK6pGHMMHTePtQtaIXU2FQxM4Gal" style="width: 250px; height: 250px max-width: 50%; height: auto" />

# Landscape
<div></div>
<a href="https://drive.google.com/uc?export=view&id=1N71ChMiwycMOxmjnoE7RJgsmraE7H1uy"><img src="https://drive.google.com/uc?export=view&id=1N71ChMiwycMOxmjnoE7RJgsmraE7H1uy" style="width: 450px; height: 250px;"/>
<br>
<a href="https://drive.google.com/uc?export=view&id=1N7RZvGZKwTVCfk3V-ZV7p7fth0iktskQ"><img src="https://drive.google.com/uc?export=view&id=1N7RZvGZKwTVCfk3V-ZV7p7fth0iktskQ" style="width: 450px; height: 250px;"/>

## FlowChart describing the logic when dealing with permissions:
<a href="https://drive.google.com/uc?export=view&id=1QnDAbwvzlp-cXYq6NxojmukoV47mhlHl"><img src="https://drive.google.com/uc?export=view&id=1QnDAbwvzlp-cXYq6NxojmukoV47mhlHl" style="width: 100%; height: 550px;"/>
<p>The <b>main concept</b> to not request permissions everytime the user opens the app. User can use the app regulary and can see photos or videos with a click on the screen. That's when i ask the user for the permissions again</p>
<p><b>When should we get the data?</b></p>
<p>I will get data from MediaStore only when the user touch the screen (when permission not granted ) or if the list is empty (i don't load data from mediastore again ) and that happens when user grant permissions on the videos screen but when getting back to images screen. I check on permissions if granted and if the list is empty so i can get the data from mediastore</p>
<p><b>isCalledOnce</b> it handled using sharedpreferences</p>
