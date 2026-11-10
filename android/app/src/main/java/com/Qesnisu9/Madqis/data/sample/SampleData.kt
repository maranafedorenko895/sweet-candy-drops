package com.Qesnisu9.Madqis.data.sample

import com.Qesnisu9.Madqis.domain.model.CandyCollectible
import com.Qesnisu9.Madqis.domain.model.CandyRarity
import com.Qesnisu9.Madqis.domain.model.Reward

object SampleData {

    val segmentTitles = listOf(
        "Marshmallow Meadow",
        "Lollipop Lane",
        "Caramel Creek",
        "Jelly Bean Bridge",
        "Nougat Nook",
        "Gumdrop Gully",
        "Toffee Terrace",
        "Sherbet Slope",
        "Praline Pass",
        "Liquorice Loop",
        "Honeycomb Hollow",
        "Bonanza Peak"
    )

    val dailyRouteTitles = listOf(
        "Sunday Sprinkle Run",
        "Monday Marzipan Mile",
        "Tuesday Toffee Trail",
        "Wednesday Waffle Walk",
        "Thursday Truffle Track",
        "Friday Fudge Field",
        "Saturday Sugar Sprint"
    )

    val rewardLadder = listOf(
        Reward(id = 1, title = "Sugar Starter Pouch", requiredRoutes = 1, coins = 20),
        Reward(id = 2, title = "Caramel Courier Badge", requiredRoutes = 3, coins = 45),
        Reward(id = 3, title = "Jelly Guild Ribbon", requiredRoutes = 6, coins = 80),
        Reward(id = 4, title = "Honeycomb Crown", requiredRoutes = 10, coins = 140),
        Reward(id = 5, title = "Bonanza Trophy Jar", requiredRoutes = 15, coins = 220)
    )

    val candyCollection = listOf(
        CandyCollectible(id = 1, title = "Bramble the Gummy Bear", rarity = CandyRarity.COMMON, treatThreshold = 0),
        CandyCollectible(id = 2, title = "Swirl the Lollipop", rarity = CandyRarity.COMMON, treatThreshold = 25),
        CandyCollectible(id = 3, title = "Pip the Jelly Heart", rarity = CandyRarity.COMMON, treatThreshold = 60),
        CandyCollectible(id = 4, title = "Tangle the Liquorice", rarity = CandyRarity.SWEET, treatThreshold = 120),
        CandyCollectible(id = 5, title = "Fizz the Sherbet Cube", rarity = CandyRarity.SWEET, treatThreshold = 200),
        CandyCollectible(id = 6, title = "Nutmeg the Praline", rarity = CandyRarity.SWEET, treatThreshold = 320),
        CandyCollectible(id = 7, title = "Comet the Rainbow Star", rarity = CandyRarity.RARE, treatThreshold = 480),
        CandyCollectible(id = 8, title = "Honeydrop the Royal", rarity = CandyRarity.RARE, treatThreshold = 700)
    )
}
