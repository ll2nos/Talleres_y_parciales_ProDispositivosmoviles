package com.ucaldas.aplicacion1

data class Nombres(
    val id: Int,
    val nombre: String,
    val imagenUrl: String,
    val descripcion: String,
    val superPoderes: List<String>
)

val listaNombres = listOf(
    Nombres(
        1,
        "Superman",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/644-superman.jpg",
        "Superman es un heroe de Superman Prime One-Million reconocido por ACTION COMICS #1.",
        listOf("Inteligencia superior", "Super fuerza", "Super velocidad", "Alta resistencia")
    ),
    Nombres(
        2,
        "Batman",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/70-batman.jpg",
        "Batman es un heroe de DC Comics reconocido por Detective Comics #27.",
        listOf("Inteligencia superior", "Combate experto")
    ),
    Nombres(
        3,
        "Wonder Woman",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/720-wonder-woman.jpg",
        "Wonder Woman es un heroe de DC Comics reconocido por All-Star Comics #8 (December, 1941).",
        listOf("Inteligencia superior", "Super fuerza", "Super velocidad", "Alta resistencia")
    ),
    Nombres(
        4,
        "Spider-Man",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/620-spider-man.jpg",
        "Spider-Man es un heroe de Marvel Comics reconocido por Amazing Fantasy #15.",
        listOf("Inteligencia superior", "Alta resistencia", "Energia o poder especial", "Combate experto")
    ),
    Nombres(
        5,
        "Iron Man",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/346-iron-man.jpg",
        "Iron Man es un heroe de Marvel Comics reconocido por Tales of Suspence #39 (March, 1963).",
        listOf("Inteligencia superior", "Super fuerza", "Alta resistencia", "Energia o poder especial")
    ),
    Nombres(
        6,
        "Captain Marvel",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/157-captain-marvel.jpg",
        "Captain Marvel es un heroe de Binary reconocido por Ms. Marvel #1.",
        listOf("Inteligencia superior", "Super fuerza", "Super velocidad", "Alta resistencia")
    ),
    Nombres(
        7,
        "Captain America",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/149-captain-america.jpg",
        "Captain America es un heroe de Marvel Comics reconocido por Captain America Comics #1 (March 1941).",
        listOf("Combate experto")
    ),
    Nombres(
        8,
        "Thor",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/659-thor.jpg",
        "Thor es un heroe de Rune King Thor reconocido por Journey into Mystery #83 (August, 1962).",
        listOf("Super fuerza", "Super velocidad", "Alta resistencia", "Energia o poder especial")
    ),
    Nombres(
        9,
        "Hulk",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/332-hulk.jpg",
        "Hulk es un heroe de Marvel Comics reconocido por Incredible Hulk #1 (May, 1962).",
        listOf("Inteligencia superior", "Super fuerza", "Alta resistencia", "Energia o poder especial")
    ),
    Nombres(
        10,
        "Black Widow",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/107-black-widow.jpg",
        "Black Widow es un heroe de Marvel Comics reconocido por Tales of Suspense #52.",
        listOf("Inteligencia superior", "Combate experto")
    ),
    Nombres(
        11,
        "Flash",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/263-flash.jpg",
        "Flash es un heroe de DC Comics reconocido por Flash Comics #1 (January 1940).",
        listOf("Super velocidad")
    ),
    Nombres(
        12,
        "Hal Jordan",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/306-hal-jordan.jpg",
        "Hal Jordan es un heroe de DC Comics reconocido por Showcase #22.",
        listOf("Super fuerza", "Super velocidad", "Alta resistencia", "Energia o poder especial")
    ),
    Nombres(
        13,
        "Aquaman",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/38-aquaman.jpg",
        "Aquaman es un heroe de DC Comics reconocido por More Fun Comics #73 (November, 1941).",
        listOf("Inteligencia superior", "Super fuerza", "Super velocidad", "Alta resistencia")
    ),
    Nombres(
        14,
        "Cyborg",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/194-cyborg.jpg",
        "Cyborg es un heroe de DC Comics reconocido por DC Comics Presents #26 (October, 1980).",
        listOf("Inteligencia superior", "Alta resistencia", "Energia o poder especial")
    ),
    Nombres(
        15,
        "Martian Manhunter",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/432-martian-manhunter.jpg",
        "Martian Manhunter es un heroe de DC Comics reconocido por Detective Comics #225 (1955).",
        listOf("Inteligencia superior", "Super fuerza", "Super velocidad", "Alta resistencia")
    ),
    Nombres(
        16,
        "Green Arrow",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/298-green-arrow.jpg",
        "Green Arrow es un heroe de DC Comics reconocido por More Fun Comics #73 (November 1941).",
        listOf("Inteligencia superior", "Combate experto")
    ),
    Nombres(
        17,
        "Black Panther",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/106-black-panther.jpg",
        "Black Panther es un heroe de Marvel Comics reconocido por Fantastic Four Vol. 1 #52 (1966).",
        listOf("Inteligencia superior", "Combate experto")
    ),
    Nombres(
        18,
        "Doctor Strange",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/226-doctor-strange.jpg",
        "Doctor Strange es un heroe de Marvel Comics reconocido por Strange Tales #110 (July, 1963).",
        listOf("Inteligencia superior", "Alta resistencia", "Energia o poder especial")
    ),
    Nombres(
        19,
        "Scarlet Witch",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/579-scarlet-witch.jpg",
        "Scarlet Witch es un heroe de Marvel Comics reconocido por Uncanny X-Men #4 (March, 1964).",
        listOf("Inteligencia superior", "Alta resistencia", "Energia o poder especial", "Combate experto")
    ),
    Nombres(
        20,
        "Vision",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/697-vision.jpg",
        "Vision es un heroe de Anti-Vision reconocido por Avengers Vol.1 #57, Young Avengers # 4.",
        listOf("Inteligencia superior", "Super fuerza", "Alta resistencia", "Energia o poder especial")
    ),
    Nombres(
        21,
        "Wolverine",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/717-wolverine.jpg",
        "Wolverine es un heroe de Marvel Comics reconocido por Incredible Hulk Vol. 2 #180.",
        listOf("Alta resistencia", "Energia o poder especial", "Combate experto")
    ),
    Nombres(
        22,
        "Storm",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/638-storm.jpg",
        "Storm es un heroe de Marvel Comics reconocido por Giant-Size X-Men #1 (May, 1975).",
        listOf("Inteligencia superior", "Energia o poder especial", "Combate experto")
    ),
    Nombres(
        23,
        "Rogue",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/567-rogue.jpg",
        "Rogue es un heroe de Marvel Comics reconocido por Avengers Annual #10 (1981).",
        listOf("Inteligencia superior", "Energia o poder especial", "Combate experto")
    ),
    Nombres(
        24,
        "Beast",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/75-beast.jpg",
        "Beast es un heroe de Marvel Comics reconocido por X-MEN Vol. 1 #1, (in furry ape form) AMAZING ADVENTURES #11, (in current feline form) NEW X-MEN Vol. 1 #114.",
        listOf("Inteligencia superior", "Combate experto")
    ),
    Nombres(
        25,
        "Nightcrawler",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/490-nightcrawler.jpg",
        "Nightcrawler es un heroe de Marvel Comics reconocido por GIANT-SIZE X-MEN #1.",
        listOf("Energia o poder especial", "Combate experto")
    ),
    Nombres(
        26,
        "Deadpool",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/213-deadpool.jpg",
        "Deadpool es un heroe de Evil Deadpool reconocido por New Mutants #98 (February, 1991).",
        listOf("Alta resistencia", "Energia o poder especial", "Combate experto")
    ),
    Nombres(
        27,
        "Daredevil",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/201-daredevil.jpg",
        "Daredevil es un heroe de Marvel Comics reconocido por Daredevil #1 (April, 1964).",
        listOf("Inteligencia superior", "Combate experto")
    ),
    Nombres(
        28,
        "Punisher",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/530-punisher.jpg",
        "Punisher es un heroe de Marvel Comics reconocido por AMAZING SPIDER-MAN #129.",
        listOf("Combate experto")
    ),
    Nombres(
        29,
        "Silver Surfer",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/598-silver-surfer.jpg",
        "Silver Surfer es un heroe de Marvel Comics reconocido por Fantastic Four, Vol. 1 #48.",
        listOf("Super fuerza", "Super velocidad", "Alta resistencia", "Energia o poder especial")
    ),
    Nombres(
        30,
        "Captain Atom",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/150-captain-atom.jpg",
        "Captain Atom es un heroe de DC Comics reconocido por Captain Atom #1.",
        listOf("Inteligencia superior", "Super fuerza", "Super velocidad", "Alta resistencia")
    ),
    Nombres(
        31,
        "Supergirl",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/643-supergirl.jpg",
        "Supergirl es un heroe de DC Comics reconocido por Superman/Batman #8 (May, 2004).",
        listOf("Inteligencia superior", "Super fuerza", "Super velocidad", "Alta resistencia")
    ),
    Nombres(
        32,
        "Batgirl",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/63-batgirl.jpg",
        "Batgirl es un heroe de Oracle reconocido por Detective Comics #359.",
        listOf("Inteligencia superior", "Combate experto")
    ),
    Nombres(
        33,
        "Robin",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/561-robin.jpg",
        "Robin es un heroe de Batman II reconocido por Detective Comics #38 (April, 1940).",
        listOf("Inteligencia superior", "Combate experto")
    ),
    Nombres(
        34,
        "Starfire",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/632-starfire.jpg",
        "Starfire es un heroe de DC Comics reconocido por DC Comics Presents #26 (October, 1980).",
        listOf("Super fuerza", "Alta resistencia", "Combate experto")
    ),
    Nombres(
        35,
        "Raven",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/542-raven.jpg",
        "Raven es un heroe de DC Comics reconocido por DC Comics Presents #26 (October, 1980).",
        listOf("Alta resistencia", "Energia o poder especial")
    ),
    Nombres(
        36,
        "Beast Boy",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/76-beast-boy.jpg",
        "Beast Boy es un heroe de DC Comics reconocido por Doom Patrol #99 (November, 1965).",
        listOf("Alta resistencia", "Energia o poder especial")
    ),
    Nombres(
        37,
        "Hawkgirl",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/315-hawkgirl.jpg",
        "Hawkgirl es un heroe de DC Comics reconocido por JSA Secret Files #1 (August 1999).",
        listOf("Alta resistencia", "Energia o poder especial", "Combate experto")
    ),
    Nombres(
        38,
        "Zatanna",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/730-zatanna.jpg",
        "Zatanna es un heroe de DC Comics reconocido por Hawkman #4.",
        listOf("Inteligencia superior", "Energia o poder especial")
    ),
    Nombres(
        39,
        "Ant-Man",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/30-ant-man.jpg",
        "Ant-Man es un heroe de Giant-Man reconocido por Tales to Astonish #27 (January, 1962) (as Hank Pym)  Tales to Astonish #35 (September, 1962) (as Ant-Man).",
        listOf("Inteligencia superior")
    ),
    Nombres(
        40,
        "Wasp",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/708-wasp.jpg",
        "Wasp es un heroe de Marvel Comics reconocido por TALES TO ASTONISH #44.",
        listOf("Habilidades especiales")
    ),
    Nombres(
        41,
        "Falcon",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/251-falcon.jpg",
        "Falcon es un heroe de Marvel Comics reconocido por Captain America #117 (September, 1969).",
        listOf("Habilidades especiales")
    ),
    Nombres(
        42,
        "Winter Soldier",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/714-winter-soldier.jpg",
        "Winter Soldier es un heroe de Marvel Comics reconocido por Captain America Vol 5 #1 (January, 2005) (as Winter Soldier).",
        listOf("Combate experto")
    ),
    Nombres(
        43,
        "Moon Knight",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/470-moon-knight.jpg",
        "Moon Knight es un heroe de Marvel Comics reconocido por Werewolf by Night #32 (August, 1975).",
        listOf("Combate experto")
    ),
    Nombres(
        44,
        "She-Hulk",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/589-she-hulk.jpg",
        "She-Hulk es un heroe de Marvel Comics reconocido por She-Hulk (Vol. 1) #1.",
        listOf("Inteligencia superior", "Super fuerza", "Alta resistencia", "Combate experto")
    ),
    Nombres(
        45,
        "Gamora",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/275-gamora.jpg",
        "Gamora es un heroe de Marvel Comics reconocido por Strange Tales #180 (June, 1975).",
        listOf("Inteligencia superior", "Super fuerza", "Alta resistencia", "Combate experto")
    ),
    Nombres(
        46,
        "Rocket Raccoon",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/566-rocket-raccoon.jpg",
        "Rocket Raccoon es un heroe de Marvel Comics reconocido por Marvel Preview #7 (June, 1976).",
        listOf("Habilidades especiales")
    ),
    Nombres(
        47,
        "Groot",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/303-groot.jpg",
        "Groot es un heroe de Marvel Comics reconocido por Tales to Astonish #13 (November, 1960).",
        listOf("Inteligencia superior", "Super fuerza", "Alta resistencia", "Energia o poder especial")
    ),
    Nombres(
        48,
        "Drax the Destroyer",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/234-drax-the-destroyer.jpg",
        "Drax the Destroyer es un heroe de Marvel Comics reconocido por Iron Man #55 (February, 1973).",
        listOf("Super fuerza", "Alta resistencia")
    ),
    Nombres(
        49,
        "Mantis",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/431-mantis.jpg",
        "Mantis es un heroe de Marvel Comics reconocido por Avengers #112.",
        listOf("Alta resistencia", "Energia o poder especial", "Combate experto")
    ),
    Nombres(
        50,
        "Nova",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/497-nova.jpg",
        "Nova es un heroe de Marvel Comics reconocido por Fantastic Four #164 (November, 1975).",
        listOf("Super velocidad", "Alta resistencia", "Energia o poder especial")
    )
)
