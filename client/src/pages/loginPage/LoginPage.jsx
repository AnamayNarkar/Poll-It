import React, { useEffect, useRef, useState } from 'react'
import { Application, Graphics } from 'pixi.js'

import FormContainer from './components/FormContainer'

const LoginPage = () => {

	const eyeTrackingAppRef = useRef(null);

	useEffect(() => {
		let appJustStarted = true;

		const mouse = { x: 0, y: 0 };

		window.addEventListener('mousemove', (event) => {
			mouse.x = event.clientX;
			mouse.y = event.clientY;
		});

		const initializePixi = async () => {
			if (eyeTrackingAppRef.current) return;

			const eyeTrackingApp = new Application();
			const parentContainer = document.querySelector('.loginPageContainer');
			await eyeTrackingApp.init({
				background: '#242424',
				resizeTo: parentContainer,
				antialias: true,
				// resolution: 1,
			});
			eyeTrackingAppRef.current = eyeTrackingApp;

			parentContainer.appendChild(eyeTrackingApp.canvas);

			const body = new Graphics()
				.lineStyle(1, 0x000001)
				.beginFill(0x00ffff)
				.drawEllipse(0, 0, 100, 140)
				.endFill();
			body.x = eyeTrackingApp.screen.width / 7;
			body.y = eyeTrackingApp.screen.height + body.height;

			const leftEar = new Graphics()
				.beginFill(0x00ffff)
				.drawEllipse(0, 0, 20, 20)
				.endFill();
			leftEar.x = -60;
			leftEar.y = -120;

			const rightEar = new Graphics()
				.beginFill(0x00ffff)
				.drawEllipse(0, 0, 20, 20)
				.endFill();
			rightEar.x = 60;
			rightEar.y = -120;

			const smallCircleInLeftEar = new Graphics()
				.beginFill(0xa78248)
				.drawEllipse(0, 0, 10, 10)
				.endFill();
			smallCircleInLeftEar.x = 0;
			smallCircleInLeftEar.y = 0;

			const smallCircleInRightEar = new Graphics()
				.beginFill(0xa78248)
				.drawEllipse(0, 0, 10, 10)
				.endFill();
			smallCircleInRightEar.x = 0;
			smallCircleInRightEar.y = 0;

			leftEar.addChild(smallCircleInLeftEar);
			rightEar.addChild(smallCircleInRightEar);

			const leftEye = new Graphics()
				.lineStyle(1, 0x000001)
				.beginFill(0xffffff)
				.drawEllipse(0, 0, 20, 25)
				.endFill();
			const rightEye = new Graphics()
				.lineStyle(1, 0x000001)
				.beginFill(0xffffff)
				.drawEllipse(0, 0, 20, 25)
				.endFill();

			leftEye.x = -35;
			leftEye.y = -75;
			rightEye.x = 35;
			rightEye.y = -75;

			const leftPupil = new Graphics()
				.beginFill(0x000001)
				.drawEllipse(0, 0, 7, 7)
				.endFill();
			leftPupil.x = 0;
			leftPupil.y = leftEye.height / 2 - 3;

			const rightPupil = new Graphics()
				.beginFill(0x000001)
				.drawEllipse(0, 0, 7, 7)
				.endFill();
			rightPupil.x = 0;
			rightPupil.y = rightEye.height / 2 - 3;

			leftEye.addChild(leftPupil);
			rightEye.addChild(rightPupil);

			body.addChild(leftEye);
			body.addChild(rightEye);

			body.addChild(leftEar);
			body.addChild(rightEar);

			const knot = new Graphics()
				.beginFill(0x000001)
				.drawPolygon([
					0, 0,
					10, -10,
					-10, -10,
				])
				.endFill();

			knot.x = 0;
			knot.y = -30;

			const tie = new Graphics()
				.beginFill(0x000001)
				.drawPolygon([
					0, -10,
					20, 80,
					-20, 80,
				])
				.endFill();

			knot.addChild(tie);

			body.addChild(knot);

			eyeTrackingApp.stage.addChild(body);

			const updatePupilPosition = (pupil, eye, mouseX, mouseY) => {
				const dx = mouseX - (eye.x + body.x);
				const dy = mouseY - (eye.y + body.y);
				const angle = Math.atan2(dy, dx);
				const distance = Math.min(10, Math.sqrt(dx * dx + dy * dy));
				const pupilX = distance * Math.cos(angle);
				const pupilY = distance * Math.sin(angle);

				pupil.x = pupilX;
				pupil.y = pupilY - 5;
			};

			eyeTrackingApp.ticker.add(() => {
				if (appJustStarted) {
					body.y -= 3;
					if (body.y < eyeTrackingApp.screen.height - 30) {
						appJustStarted = false;
					}
				}
				updatePupilPosition(leftPupil, leftEye, mouse.x, mouse.y);
				updatePupilPosition(rightPupil, rightEye, mouse.x, mouse.y);
			});
		};

		initializePixi();

		return () => {
			if (eyeTrackingAppRef.current != null) {
				eyeTrackingAppRef.current.destroy(true, {
					children: true,
					texture: true,
					baseTexture: true,
				});
				eyeTrackingAppRef.current = null;
			}
		};
	}, []);

	return (
		<div className='loginPageContainer' style={{
			width: '100vw',
			height: '100vh',
			margin: 0,
			padding: 0,
			position: 'relative',
			overflow: 'hidden',
		}}>
			<FormContainer />
		</div>
	)
}

export default LoginPage

