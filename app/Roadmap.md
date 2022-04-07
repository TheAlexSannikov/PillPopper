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

# Future plans: PillPopper in "Hi Mom!" tab
    Sealed class for drugs:
        {name, periodHrs, maxDosageMgPerPeriod, maxDosageMgPerDay}
    pills:
        {drug, dosageMg, timeTakenMsEpoch}

    Room (and later DataStoreManager) behind a repository
        pill  |    time taken

    Ability to undo taking a pill

    ?? Create seperate ViewManagers for each tab ??

    Add a splashscreen!