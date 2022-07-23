About this project:
    MVVM architecture
    Two standalone apps, "StonerStopwatch" and "PillPopper"
    
    StonerStopwatch:
        As of now, a simple stopwatch that has a progress arc around a cute 'happy earth'

    PillPopper:
        More complex app, allows for user to track verious different pills

    StonerStopwatchV2
        Future app [orignal idea] Would be a CLI architecture, builds off experiences from both previous apps
        

# Styles (.xml, deprecated)

	Base.Theme.StonerStopwatch
		Theme.StonerStopwatch
		night\Theme.StonerStopwatch

# Styles
	MainActivity
		Theme.kt
			AppColors
			AppDimensions
			AppTypography

# Navigation
	MainActivity:onCreate
		MainScreenBottomNav
			BottomNavGraph
				BottomBarScreen (sealed class)

# MutableLiveData / Persistence
	MainActivity
		StopwatchViewModel 			(MutableLiveData (depricated) / MutableStateFlow)
			dataStoreManager 	(DataStore) TODO: DataLayer?

# PillPopper in "Hi Mom!" tab
    enum class for drugs
    data class for pills    [Room entity]

    PillTimerScreen
        pillViewModel
            PillDropOffWorker
                pillRepository  
                    pillDao     [Room]
            pillRepository  
                pillDao     [Room]


# TODO:
    Add a splashscreen!
    
    PillPopper functionality:
        Decide when pill cooldowns roll off 
            1) set amount of hours
            2) max dosage / day
        
        Change dosage by long hold on pill; remember this

    How to handle pills dropping off?
        Need to be able to determine amount taken in PillRepository s.t. worker can utilize this
        Just need read the sums, not a flow at this point to determine if time to notify user

    Notifications:
        notify only when user was 'disallowed' from popping before dropoff
        TODO: pill color (large icons)

    Change dosage:
        Long-press on pill allows to choose dosage. (1x), (2x), (3x), (custom) dosages available
        Custom tab lets you change the default dosage. This is persisted.
            Idea: This becomes the new (1x) dosage
    
        This requires PillPopper's Mode [dosageSelectMode, pillPopMode]

    Refactor: separate StonerStopwatch from PillPopper
        Prepare for StonerStopwatchV2
        Publish StonerStopwatchV1?
    