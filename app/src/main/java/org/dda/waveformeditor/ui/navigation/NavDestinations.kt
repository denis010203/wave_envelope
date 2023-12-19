package org.dda.waveformeditor.ui.navigation

import android.util.Base64
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

object NavDestinations {
    val fileList = navNode(route = "fileList")
    val waveEditor = navNode(route = "waveEditor", argumentName = "uri")
}

interface NavNode {
    val route: String
}

interface NavNodeSingleArgument {
    val route: String
    val navigatePath: String
    val argumentName: String
}


fun navNode(route: String): NavNode {
    return object : NavNode {
        override val route = route
    }
}

fun navNode(route: String, argumentName: String): NavNodeSingleArgument {
    return object : NavNodeSingleArgument {
        override val route = "$route/{$argumentName}"
        override val navigatePath = route
        override val argumentName = argumentName
    }
}

fun NavNodeSingleArgument.getArgumentString(
    backStackEntry: NavBackStackEntry
): String? {
    return backStackEntry.arguments?.getString(argumentName)?.toDecoded()
}

fun NavHostController.navigate(navNode: NavNodeSingleArgument, argument: String) {
    navigate("${navNode.navigatePath}/${argument.toEncoded()}")
}

private fun String.toEncoded(): String = Base64.encodeToString(toByteArray(), Base64.URL_SAFE)
private fun String.toDecoded(): String = String(Base64.decode(this, Base64.URL_SAFE))