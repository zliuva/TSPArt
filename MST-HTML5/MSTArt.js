var MSTArt = {
	pixelate: function(image) {
		var nodes = [];
		var slidingWindow = 4;
		var threshold = 4;
		var canvas = image[0];
		var context = canvas.getContext('2d');
		var imageData = context.getImageData(0, 0, image.width(), image.height());

		var id = 0;

		for (var i = 0; i < imageData.width; i += slidingWindow) {
			for (var j = 0; j < imageData.height; j += slidingWindow) {
				var count = 0;
				var hasBlack = false;

				for (var k = i; k < imageData.width && k < i + slidingWindow; k++) {
					for (var m = j; m < imageData.height && m < j + slidingWindow; m++) {
						var offset = (k + m * imageData.width) * 4;

						var r = imageData.data[offset];
						var g = imageData.data[offset + 1];
						var b = imageData.data[offset + 2];
						var a = imageData.data[offset + 3];

						var grayScale = .3 * r + .59 * g + .11 * b;

						if (grayScale < 128) {
							count++;
						}

						if (count > threshold) {
							var x = k, y = m;

							x += Math.floor(Math.random() * slidingWindow / 2);
							y += Math.floor(Math.random() * slidingWindow / 2);

							if (x >= imageData.width) {
								x = imageData.width - 1;
							}

							if (y >= imageData.height) {
								y = imageData.heigh - 1;
							}

							nodes.push({ x: x, y: y, id: id++});
							hasBlack = true;

							break;
						}
					}

					if (hasBlack) {
						break;
					}
				}
			}
		}

		return nodes;
	},
	
	calculateDistances: function(nodes) {
		var distances = new Array(nodes.length);

		for (var i = 0; i < nodes.length; i++) {
			distances[i] = new Array(nodes.length);
		}

		for (var i = 0; i < nodes.length; i++) {
			for (var j = i + 1; j < nodes.length; j++) {
				var distance = (nodes[i].x - nodes[j].x) * (nodes[i].x - nodes[j].x) +
								 (nodes[i].y - nodes[j].y) * (nodes[i].y - nodes[j].y);

				distances[nodes[i].id][nodes[j].id] = distance;
				distances[nodes[j].id][nodes[i].id] = distance;
			}
		}

		return distances;
	},
	
	solveMST: function(nodes) {
		var distances = this.calculateDistances(nodes);
		var used = new Array(nodes.length);

		for (var i = 0; i < nodes.length; i++) {
			nodes[i].key = Number.MAX_VALUE;
			used[i] = false;
		}

		nodes[0].key = 0;
		nodes[0].prev = null;

		var Q = new BinaryHeap(function(node) { return node ? node.key : 0; }, nodes.length);
		nodes.forEach(function(node) {
			Q.push(node);
		});

		while (Q.size()) {
			var u = Q.pop();
			used[u.id] = true;

			nodes.forEach(function(v) {
				if (!used[v.id] && distances[u.id][v.id] < v.key) {
					Q.remove(v);
					v.key = distances[u.id][v.id];
					Q.push(v, v.key);
					v.prev = u;
				}
			});
		}

		return nodes;
	}
};

