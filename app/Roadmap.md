# Styles (.xml, depricated)

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
		StateViewModel 			(MutableLiveData (depricated) / MutableStateFlow)
			dataStoreManager 	(DataStore) TODO: DataLayer?
