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
            pillRepository  
                pillDao     [Room]


# TODO:
    Add a splashscreen!