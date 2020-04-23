package ru.drrey.babyname.navigation

/**
 * Interface used in navigation.
 * For these fragments, we add them on top of the previous one
 * if the previous one is not also an SecondaryNavigationFragment or a TertiaryNavigationFragment or a QuaternaryNavigationFragment.
 */
interface SecondaryNavigationFragment

/**
 * Interface used in navigation.
 * For these fragments, we add them on top of the previous one
 * if the previous one is not also an TertiaryNavigationFragment or a QuaternaryNavigationFragment
 */
interface TertiaryNavigationFragment


/**
 * Interface used in navigation.
 * For these fragments, we add them on top of the previous one
 * if the previous one is not also an QuaternaryNavigationFragment
 */
interface QuaternaryNavigationFragment

/**
 * Interface used in navigation.
 * Fragment with alpha push/pop animations.
 */
interface AlphaNavigationFragment