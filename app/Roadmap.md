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

    