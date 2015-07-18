/**
 * Created by Robert on 14.02.15.
 */

var gulp = require('gulp');
var sass = require('gulp-sass');
var minifyCSS = require('gulp-minify-css');
var rename = require('gulp-rename');
var sourcemaps = require('gulp-sourcemaps');
var autoprefixer = require('gulp-autoprefixer');

gulp.task('sass', function () {
    gulp.src('styles/*.scss')
        .pipe(sourcemaps.init())
        .pipe(sass())
        .pipe(autoprefixer({
            browsers: ['last 2 versions', 'ie 9'],
            cascade: false
        }))
       // .pipe(minifyCSS())
       // .pipe(rename('style.min.css'))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest('dist/styles'));
});

var gulpif = require('gulp-if');
var sprite = require('css-sprite').stream;

// generate sprite.png and _sprite.scss
gulp.task('sprites', function () {
    return gulp.src('images/*.jpeg')
        .pipe(sprite({
            name: 'sprite', // File name
            style: '_sprite.scss', // File with styles
            cssPath: 'images', // background-image will use this as url to sprite
            processor: 'scss', // Styles output format
            margin: 0, // Margin between icons in sprite
            orientation: 'binary-tree' // Sprite icons orientation
        }))
        // This pipe will output both sprite.png and _sprite.scss so we want to save those files in different locations
        .pipe(gulpif('*.jpeg', gulp.dest('images/'), gulp.dest('dist/styles/')))
});
