package de.hochschuletrier.gdw.ws1415.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;

import de.hochschuletrier.gdw.ws1415.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionInLevelComponent;
import de.hochschuletrier.gdw.ws1415.game.components.TileComponent;
import de.hochschuletrier.gdw.ws1415.game.utils.GameBoardInformation;

public class MovementUtil {

	static ImmutableArray<Entity> tileEntities;
	static PooledEngine engine;

	public static void init(PooledEngine engine, ImmutableArray<Entity> tE) {
		tileEntities = tE;
		MovementUtil.engine = engine;
		System.out.println(tileEntities.size());
	}

	public static void moveH(int row, float direction) {

		for (Entity entity : tileEntities) {

			if (entity.getComponent(PositionInLevelComponent.class).x == row) {
				MovementComponent moveComponent = engine
						.createComponent(MovementComponent.class);
				entity.add(moveComponent);
				moveComponent.originX = entity
						.getComponent(PositionComponent.class).x;
				moveComponent.originY = entity
						.getComponent(PositionComponent.class).y;
				moveComponent.destinationX = entity
						.getComponent(PositionComponent.class).x
						+ GameBoardInformation.TILE_SIZE * direction;
				moveComponent.destinationY = entity
						.getComponent(PositionComponent.class).y;
			}

		}

	}

	public static void moveV(int row, float direction) {

		for (Entity entity : tileEntities) {

			if (entity.getComponent(PositionInLevelComponent.class).y == row) {
				MovementComponent moveComponent = engine
						.createComponent(MovementComponent.class);
				entity.add(moveComponent);
				moveComponent.originX = entity
						.getComponent(PositionComponent.class).x;
				moveComponent.originY = entity
						.getComponent(PositionComponent.class).y;
				moveComponent.destinationX = entity
						.getComponent(PositionComponent.class).x
						+ GameBoardInformation.TILE_SIZE * direction;
				moveComponent.destinationY = entity
						.getComponent(PositionComponent.class).y;
			}

		}

	}

}
