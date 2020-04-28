package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	private SpriteBatch batch;
	private ShapeRenderer shape;

	//Declaração das Texturas
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Texture gameOver;
	private BitmapFont fonte;
	private Circle passaroCirculo;
	private Rectangle retanguloCanoBaixo;
	private Rectangle retanguloCanoTopo;

	//Atributos de Configuração
	private float larguraDispositivo;
	private float alturaDispositivo;
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

	//Camera
	private OrthographicCamera camera;
	private Viewport viewport;
	private final float VIRTUAL_WIDTH = 768;
	private final float VIRTUAL_HEIGHT = 1024;

	@Override
	public void create () {
		batch = new SpriteBatch();
		inicializarTexturas();
		inicializarConfiguracoes();
	}

	@Override
	public void render () {
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		deltaTime = Gdx.graphics.getDeltaTime();

		if (estadoJogo == 0){ //JOGO PARADO ATÉ TOCAR NA TELA
				if(Gdx.input.justTouched()) estadoJogo = 1;
			} else {
				velocidadeQueda++;
				if(posicaoInicialVertical > 0 || velocidadeQueda < 0) posicaoInicialVertical -= velocidadeQueda;

				if(estadoJogo == 1){ //JOGO RODANDO
					posicaoMovimentoCanoHorizontal -= deltaTime*500;
					configCanoTexturas();
					configPassaroTexturas();
					if (Gdx.input.justTouched()) velocidadeQueda = -15;
					verificarPontuacao();
				}
		}
		//Chamando as texturas com parâmetros
		batch.draw(fundo, 0,0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int) variacao], 100, posicaoInicialVertical);
		batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo/2 + espacoCanos/2 + alturaEntreCanosRandom);
		batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo /2 - canoBaixo.getHeight() - espacoCanos/2 + alturaEntreCanosRandom);
		fonte.draw(batch, String.valueOf(score), larguraDispositivo/2, alturaDispositivo - 100);

		if (estadoJogo == 2){
			batch.draw(gameOver, larguraDispositivo/2 - 200, alturaDispositivo/2);
		}
		//FINALIZANDO A RENDERIZAÇÃO
		batch.end();

		desenharFormas();

		detectarColisoes();
	}

	@Override
	public void resize(int width, int height){
		viewport.update(width, height);
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

		//Inicialização do Game Over
		gameOver = new Texture("game_over.png");

		//Inicialização do ShapeRender
		shape = new ShapeRenderer();
	}

	private void inicializarConfiguracoes(){
		//Dispositivo
		larguraDispositivo = VIRTUAL_WIDTH;
		alturaDispositivo = VIRTUAL_HEIGHT;
		numeroRandomico = new Random();

		//CAMERA
		camera = new OrthographicCamera();
		camera.position.set(VIRTUAL_WIDTH/2, VIRTUAL_HEIGHT/2, 0);
		viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

		//Posição do Pássaro
		posicaoInicialVertical = alturaDispositivo/2;

		//Posição do Cano
		posicaoMovimentoCanoHorizontal = larguraDispositivo - 100;

        //Espaçamento Entre Canos
        espacoCanos = 250;
	}

	private void configPassaroTexturas(){
        //CONFIGURAÇÃO DE RENDERIZAÇÃO
        //Espaçamento entre as Texturas dos Pássaros
        variacao += deltaTime*10;
        //Loop - Texturas dos Pássaros
        if(variacao > 2) variacao = 0;
    }

    private void configCanoTexturas(){
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
        /* Inicialização do ShapeRender (PARA TESTES)
        shape = new ShapeRenderer();
         */

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

        /* PARA TESTES
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
		*/
	}

	private void detectarColisoes(){
		if (Intersector.overlaps(passaroCirculo, retanguloCanoBaixo) || Intersector.overlaps(passaroCirculo, retanguloCanoTopo)){
			estadoJogo = 2;
		}
	}
}