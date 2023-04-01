package com.example.electriccarapp.data

import com.example.electriccarapp.domain.Carro

object CarFactory {

    val list = listOf<Carro>(
        Carro(
            id = 1,
            preco = "$300000.00",
            bateria = "300kWh",
            potencia = "200cv",
            recarga = " 30 min",
            urlFoto = "www.google.com.br"
        ),
        Carro(
            id = 2,
            preco = "$200000.00",
            bateria = "200kWh",
            potencia = "150cv",
            recarga = " 10 min",
            urlFoto = "www.google.com.br"
        ),
        Carro(
            id = 3,
            preco = "$100000.00",
            bateria = "100kWh",
            potencia = "50cv",
            recarga = " 11 min",
            urlFoto = "www.google.com.br"
        ),
        Carro(
            id = 4,
            preco = "$90000.00",
            bateria = "90kWh",
            potencia = "25cv",
            recarga = " 15 min",
            urlFoto = "www.google.com.br"
        )
    )
}