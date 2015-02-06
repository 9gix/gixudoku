var cell = {
}

var sudoku = {
    // 2d array that hold the sudoku data
    board: [],

    /***
     * Configuration
     */
    config: {
        size: 9,
    },

    /***
     * Constructor
     */
    init: function(){
        var question = "845237096971546823236189475123894657654723981789605342510468730090350008308972514";
        this.initBoard(question);
    },

    /***
     * Generate Board
     */
    initBoard: function(quest_str){
        data = quest_str.split('');
        for (var i = 0; i < this.config.size * this.config.size; i+= 1){
            if (data[i] === '0'){
                data[i] = null;
            } else {
                data[i] = parseInt(data[i]);
            }
        }
        this.board = [];
        for (var i = 0; i < this.config.size * this.config.size; i+= this.config.size){
            this.board.push(data.slice(i, i+this.config.size));
        }
    },

    /***
     * Render the Board into the Canvas
     */
    getBoard: function(){
        return this.board;
    },

    /***
     * Destructor
     */
    destroy: function(){
        this.board = [];
    },
};

var SudokuGame = (function(){
    var width = 540;
    var height = width;
    var padding = 10;
    var x_inc = width / sudoku.config.size;
    var y_inc = height / sudoku.config.size;

    function newGame(){
        sudoku.init();
    }

    function drawBoard(ctx){
        for (var x = 0; x <= width; x += x_inc) {
            ctx.moveTo(x + padding, padding);
            ctx.lineTo(x + padding, height + padding);
        }

        for (var y = 0; y <= height; y += y_inc) {
            ctx.moveTo(padding, y + padding);
            ctx.lineTo(width + padding, y + padding);
        }

        ctx.strokeStyle = "black";
        ctx.stroke();
    }

    function drawNumber(ctx){
        ctx.font = '30 px';
        for (var x = 0; x < sudoku.config.size; x += 1){
            for (var y = 0; y < sudoku.config.size; y += 1){
                if (sudoku.board[x][y] !== null){
                    ctx.fillText(sudoku.board[x][y], 30 + x * x_inc, 50 + y * y_inc);
                }
            }
        }
    }

    function render(){
        var board = sudoku.getBoard();
        var cvs = document.getElementById('sudoku-canvas');
        var ctx = cvs.getContext('2d');
        cvs.width = width + (padding * 2) + 1;
        cvs.height = height + (padding * 2) + 1;

        drawBoard(ctx);
        drawNumber(ctx);
    }

    function endGame(){
        sudoku.destroy();
    }

    function init(){
        newGame();
        render();
    }

    return {
        init: init,
    };
})();

document.addEventListener('DOMContentLoaded', function(){
    SudokuGame.init();
});

// Sample Data (Q|A)
// 845237096971546823236189475123894657654723981789605342510468730090350008308972514
// 845237196971546823236189475123894657654723981789615342512468739497351268368972514
