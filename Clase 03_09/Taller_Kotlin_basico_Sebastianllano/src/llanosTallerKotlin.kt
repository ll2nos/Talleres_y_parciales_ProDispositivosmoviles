
    fun ejercicio1Temperatura() {
        val temperaturaCelsius = 25.0
        val temperaturaFahrenheit = temperaturaCelsius * 1.8 + 32

        println("${temperaturaCelsius}°C equivale a ${temperaturaFahrenheit}°F")
    }

    fun ejercicio2IMC() {
        val pesoKg = 70.0
        val alturaMetros = 1.75
        val imc = pesoKg / (alturaMetros * alturaMetros)

        val clasificacion = if (imc < 18.5) {
            "Bajo peso"
        } else if (imc < 25.0) {
            "Peso normal"
        } else if (imc < 30.0) {
            "Sobrepeso"
        } else {
            "Obesidad"
        }

        println("Peso: ${pesoKg}kg, Altura: ${alturaMetros}m")
        println("IMC: %.2f".format(imc))
        println("Clasificación: $clasificacion")
    }

    fun ejercicio3ParImpar() {
        val numeroEvaluado = 42
        val esImpar = numeroEvaluado % 2 != 0

        println("El número $numeroEvaluado es: ${if (esImpar) "Impar" else "Par"}")
    }

    fun esPrimo(valor: Int): Boolean {
        if (valor <= 1) return false
        if (valor <= 3) return true
        if (valor % 2 == 0) return false

        var divisor = 3
        while (divisor * divisor <= valor) {
            if (valor % divisor == 0) return false
            divisor += 2
        }
        return true
    }

    fun ejercicio4NumeroPrimo() {
        val candidato = 17

        if (esPrimo(candidato)) {
            println("El número $candidato ES primo")
        } else {
            println("El número $candidato NO es primo")
        }
    }

    fun ejercicio5SumaPares() {
        var acumulado = 0
        var contador = 1

        while (contador <= 100) {
            if (contador % 2 == 0) acumulado += contador
            contador++
        }

        println("La suma de números pares del 1 al 100 es: $acumulado")
    }

    fun factorialDe(n: Int): Long {
        if (n <= 1) return 1L
        return n * factorialDe(n - 1)
    }

    fun ejercicio6Factorial() {
        val base = 5
        val resultado = factorialDe(base)

        println("El factorial de $base es: $resultado")
    }

    fun ejercicio7Palindromo() {
        val texto = "anilina"
        var izquierda = 0
        var derecha = texto.length - 1
        var esPalindromo = true

        while (izquierda < derecha) {
            if (texto[izquierda] != texto[derecha]) {
                esPalindromo = false
                break
            }
            izquierda++
            derecha--
        }

        println("La palabra '$texto' ${if (esPalindromo) "ES un palíndromo" else "NO es un palíndromo"}")
    }

    fun main() {
        println("=".repeat(50))
        println("TALLER 1: KOTLIN FUNDAMENTALS")
        println("Nombre: Sebastian Llano Marin")
        println("=".repeat(50))
        println()

        println("--- Ejercicio 1: Conversor de Temperatura ---")
        ejercicio1Temperatura()
        println()

        println("--- Ejercicio 2: Calculadora de IMC ---")
        ejercicio2IMC()
        println()

        println("--- Ejercicio 3: Número Par o Impar ---")
        ejercicio3ParImpar()
        println()

        println("--- Ejercicio 4: Números Primos ---")
        ejercicio4NumeroPrimo()
        println()

        println("--- Ejercicio 5: Suma de Números Pares ---")
        ejercicio5SumaPares()
        println()

        println("--- Ejercicio 6: Factorial ---")
        ejercicio6Factorial()
        println()

        println("--- Ejercicio 7: Palindromo ---")
        ejercicio7Palindromo()
        println()

        println("=".repeat(50))
        println("FIN DEL TALLER")
        println("=".repeat(50))
    }
