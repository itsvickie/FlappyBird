package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	private SpriteBatch batch;

	//Declaração das Texturas
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;

	//Atributos de Configuração
	private int larguraDispositivo;
	private int alturaDispositivo;
	private float deltaTime;
	private Random numeroRandomico;
	private int alturaEntreCanosRandom;

	//Configurações de Movimento
	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialVertical;
	private float posicaoMovimentoCanoHorizontal;
	private float espacoCanos;

	@Override
	public void create () {
		batch = new SpriteBatch();
		inicializarTexturas();
		inicializarConfiguracoes();
	}

	@Override
	public void render () {
		deltaTime = Gdx.graphics.getDeltaTime();
		configCanoTexturas();
		configPassaroTexturas();

		if (Gdx.input.justTouched()) velocidadeQueda = -15;

		//INICIANDO A RENDERIZAÇÃO
		batch.begin();

		//Chamando as texturas com parâmetros
		batch.draw(fundo, 0,0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int) variacao], 30, posicaoInicialVertical);
		batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo/2 + espacoCanos/2 + alturaEntreCanosRandom);
		batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo /2 - canoBaixo.getHeight() - espacoCanos/2 + alturaEntreCanosRandom);

		//FINALIZANDO A RENDERIZAÇÃO
		batch.end();
	}

	private void inicializarTexturas(){
        //DECLARAÇÃO DE TEXTURAS
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");

		canoBaixo = new Texture("cano_baixo.png");
		canoTopo = new Texture("cano_topo.png");
	}

	private void inicializarConfiguracoes(){
		//Dispositivo
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		numeroRandomico = new Random();

		//Posição do Pássaro
		posicaoInicialVertical = alturaDispositivo/2;

		//Posição do Cano
		posicaoMovimentoCanoHorizontal = larguraDispositivo - 100;

        //Espaçamento Entre Canos
        espacoCanos = 300;
	}

	private void configPassaroTexturas(){
        //CONFIGURAÇÃO DE RENDERIZAÇÃO
        //Espaçamento entre as Texturas dos Pássaros
        variacao += deltaTime*10;
        velocidadeQueda++;
        //Loop - Texturas dos Pássaros
        if(variacao > 2) variacao = 0;

        //Movimentação do Pássaro
        if(posicaoInicialVertical > 0 || velocidadeQueda < 0) posicaoInicialVertical -= velocidadeQueda;
    }

    private void configCanoTexturas(){
		posicaoMovimentoCanoHorizontal -= deltaTime*200;

		if(posicaoMovimentoCanoHorizontal < -canoTopo.getWidth()){
			posicaoMovimentoCanoHorizontal = larguraDispositivo;
			alturaEntreCanosRandom = numeroRandomico.nextInt(400) - 200;
		}
    }
}
