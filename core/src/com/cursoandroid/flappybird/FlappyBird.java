package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	private SpriteBatch batch;
	private ShapeRenderer shape;

	//Declaração das Texturas
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private BitmapFont fonte;
	private Circle passaroCirculo;
	private Rectangle retanguloCanoBaixo;
	private Rectangle retanguloCanoTopo;

	//Atributos de Configuração
	private int larguraDispositivo;
	private int alturaDispositivo;
	private float deltaTime;
	private Random numeroRandomico;
	private int alturaEntreCanosRandom;
	private int score = 0;
	private boolean marcouPonto;

	//Configurações de Movimento
	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialVertical;
	private float posicaoMovimentoCanoHorizontal;
	private float espacoCanos;
	private int estadoJogo = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		inicializarTexturas();
		inicializarConfiguracoes();
	}

	@Override
	public void render () {
		if (estadoJogo == 0){
			if(Gdx.input.justTouched()) estadoJogo = 1;
		} else {
			deltaTime = Gdx.graphics.getDeltaTime();
			configCanoTexturas();
			configPassaroTexturas();
			if (Gdx.input.justTouched()) velocidadeQueda = -15;
			verificarPontuacao();
		}

		//INICIANDO A RENDERIZAÇÃO
		batch.begin();

		//Chamando as texturas com parâmetros
		batch.draw(fundo, 0,0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int) variacao], 100, posicaoInicialVertical);
		batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo/2 + espacoCanos/2 + alturaEntreCanosRandom);
		batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo /2 - canoBaixo.getHeight() - espacoCanos/2 + alturaEntreCanosRandom);
		fonte.draw(batch, String.valueOf(score), larguraDispositivo/2, alturaDispositivo - 100);

		//FINALIZANDO A RENDERIZAÇÃO
		batch.end();

		desenharFormas();
	}

	private void inicializarTexturas(){
        //DECLARAÇÃO DE TEXTURAS
		//Inicialização dos Pássaros
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		//Inicialização do Fundo
		fundo = new Texture("fundo.png");

		//Inicialização dos Canos
		canoBaixo = new Texture("cano_baixo.png");
		canoTopo = new Texture("cano_topo.png");

		//Criação da Fonte e suas características
		fonte = new BitmapFont();
		fonte.setColor(Color.WHITE);
		fonte.getData().setScale(6);

		//Círculo - Colisão
		passaroCirculo = new Circle();

		//Retangulo Baixo - Colisão


		//Retangulo Topo - Colisão
		retanguloCanoTopo = new Rectangle(
				posicaoMovimentoCanoHorizontal, //DECLARAÇÃO DO X
				alturaDispositivo/2 + espacoCanos/2 + alturaEntreCanosRandom, //DECLARAÇÃO DO Y
				canoTopo.getWidth(), //DECLARAÇÃO DA LARGURA
				canoTopo.getHeight() //DECLARAÇÃO DA ALTURA
		);

		//Retangulo Topo - Colisão
		retanguloCanoTopo = new Rectangle();

		//Inicialização do ShapeRender
		shape = new ShapeRenderer();
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
		posicaoMovimentoCanoHorizontal -= deltaTime*500;

		if(posicaoMovimentoCanoHorizontal < -canoTopo.getWidth()){
			posicaoMovimentoCanoHorizontal = larguraDispositivo;
			alturaEntreCanosRandom = numeroRandomico.nextInt(400) - 200;
			marcouPonto = false;
		}
    }

    private void verificarPontuacao(){
		if(posicaoMovimentoCanoHorizontal < 100){
			if(!marcouPonto) score++; marcouPonto = true;
		}
	}

	private void desenharFormas(){
        //Inicialização do ShapeRender
        shape = new ShapeRenderer();

        //Círculo - Forma
        passaroCirculo = new Circle();
        passaroCirculo.set(100 + passaros[0].getWidth()/2, posicaoInicialVertical + passaros[0].getHeight()/2, passaros[0].getWidth()/2);

        //Retangulo Baixo - Forma
        retanguloCanoBaixo = new Rectangle(
                posicaoMovimentoCanoHorizontal, //DECLARAÇÃO DO X
                alturaDispositivo /2 - canoBaixo.getHeight() - espacoCanos/2 + alturaEntreCanosRandom, //DECLARAÇÃO DO Y
                canoBaixo.getWidth(), //DECLARAÇÃO DA LARGURA
                canoBaixo.getHeight() //DECLARAÇÃO DA ALTURA
        );

        //Retangulo Topo - Forma
        retanguloCanoTopo = new Rectangle(
                posicaoMovimentoCanoHorizontal, //DECLARAÇÃO DO X
                alturaDispositivo/2 + espacoCanos/2 + alturaEntreCanosRandom, //DECLARAÇÃO DO Y
                canoTopo.getWidth(), //DECLARAÇÃO DA LARGURA
                canoTopo.getHeight() //DECLARAÇÃO DA ALTURA
        );

        //Cor da Forma
		shape.setColor(Color.RED);

		//Inicialiação das Formas
		shape.begin(ShapeRenderer.ShapeType.Filled);

		//Círculo
		shape.circle(passaroCirculo.x, passaroCirculo.y, passaroCirculo.radius);

		//Retângulo Baixo
		shape.rect(retanguloCanoBaixo.x, retanguloCanoBaixo.y, retanguloCanoBaixo.width, retanguloCanoBaixo.height);

		//Retângulo Topo
		shape.rect(retanguloCanoTopo.x, retanguloCanoTopo.y, retanguloCanoTopo.width, retanguloCanoTopo.height);

		//Finalização das Forma
		shape.end();
	}
}