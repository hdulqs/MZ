var gulp = require('gulp');
var uglify = require('gulp-uglify');
var pump = require('pump');
var concat = require('gulp-concat');
var uglifycss = require('gulp-uglifycss');
var del = require('del'); 
var concat = require('gulp-seajs-concat');//合并模块
var transport = require('gulp-seajs-transport');//将匿名模块变成具名模块


gulp.task('clean', function (cb) {
  del([
    'dist/**/*',
    // 我们不希望删掉这个文件，所以我们取反这个匹配模式
    '!dist/mobile/deploy.json',
    'concat/**/*'
  ], cb);
});

//复制src文件下的所有的文件到dist中
gulp.task('copy', function(cb) {
  // 将你的默认的任务代码放在这
  pump([
        gulp.src('src/**/*'),
        gulp.dest('./dist')
    ],
    cb
  );
});


//压缩src下的全部js文件复制到dist中
//mangle排除  压缩seajs
gulp.task('one',['copy'], function(cb) {
  // 将你的默认的任务代码放在这
  pump([
        gulp.src(['concat/**/*.js','src/!(js)/**/*.js']),
        //gulp.src('src/**/*.js'),
        uglify({
          mangle: {except: ['require' ,'exports' ,'module' ,'$','trigger','click']}
         // mangle: {reserved: ['require' ,'exports' ,'module' ,'$']}
        }),
        gulp.dest('./dist')
    ],
    cb
  );
});



gulp.task('concat',['one'] ,function() {
  return gulp.src(['./dist/lib/jquery/jquery.js', 
                   './dist/lib/layer/layer.js',
                   './dist/lib/bootstrap/bootstrap.js',
                   './dist/lib/sb_admin2/metisMenu.js',
                   './dist/lib/sb_admin2//sb-admin-2.js',
                   './dist/lib/ztree/jquery.ztree.all-3.5.min.js',
                   './dist/lib/bootstrap-table/bootstrap-table.js',
                   './dist/lib/bootstrap-table/bootstrap-table-zh-CN.js',
                   './dist/lib/bootstrap-table/bootstrap-table-export.js',
                   './dist/lib/bootstrap-table/tableExport.js',
                   './dist/lib/bootstrap-table/bootstrap-table-editable.js',
                   './dist/lib/bootstrap-table/bootstrap-editable.js',
                   './dist/lib/bootstrap-table/ga.js'

                   ])
    .pipe(concat('lib.js'))
    .pipe(gulp.dest('./dist/'));
});


//压缩css
gulp.task('css',['copy'], function () {
  gulp.src('./src/**/*.css')
    .pipe(uglifycss({
      "maxLineLen": 1000,
      "uglyComments": true
    }))
    .pipe(gulp.dest('./dist'));
});

//合并seajs
gulp.task('seajs', function () { 
	gulp.src(['src/!(lib)/**/*.js'])  //base：  类型：String  设置输出路径以某个路径的某个组成部分为基础向后拼接
	.pipe(transport())//匿名变具名
  .pipe(concat({
      base: 'src/'
  }))  
    .pipe(gulp.dest('./concat'))  
}); 

gulp.task("default",['seajs','copy','one','css']);