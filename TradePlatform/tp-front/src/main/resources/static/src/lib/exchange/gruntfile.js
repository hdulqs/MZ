module.exports = function(grunt){
    /*
    *载入任务的插件
    * */
    grunt.loadNpmTasks('grunt-cmd-transport');  //CMD模块处理
    grunt.loadNpmTasks('grunt-cmd-concat');     //CMD模块合并
    grunt.loadNpmTasks('grunt-contrib-uglify'); //压缩

    /*
    * 项目配置
    * */
    grunt.initConfig({
        meta: {
            jsPath   : 'src/script/',
            jsDist   : 'dist/script/',
            scssPath : 'src/style/',
            cssDist  : 'dist/style/',
            jsBuild  : '.build/',
            cssCache : '.sass-cache'
        },
        /*CMD模块转换*/
        transport:{
            options: {
                debug: false,
                paths:['']
            },
            dev:{
                options:{
                    idleading: 'static/js/exchange/dist/script/' //添加ID前缀
                },
                files:[{
                    cwd: '<%= meta.jsPath %>',
                    src: ['!sea.js','module**.js','page**.js','!jquery.js'],
                    filter: 'isFile',
                    expand: true,
                    dest: '<%= meta.jsBuild %>'
                }]
            }
        },
        /*CMD模块合并*/
        concat:{
            dist: {
                options: {
                    paths:'../<%= meta.jsPath %>',
                    include: 'relative'
                },
                files:[{
                    cwd: '<%= meta.jsBuild %>',
                    src: ['**','!module**.js'],
                    filter: 'isFile',
                    expand: true,
                    dest: '<%= meta.jsDist %>'
                }]
            }
        },
        /*JS压缩*/
        uglify:{
            dist:{
                options: {
                    mangle: true, //混淆变量名
                    preserveComments: false //all:不删除注释, false:删除全部注释, some:保留@preserve @license @cc_on等注释
                },
                files: [{
                    expand:true,
                    cwd:'<%= meta.jsDist %>',
                    src:['**/*.js','!module**.js','!sea.js'],
                    dest: '<%= meta.jsDist %>'
                }]
            }
        }
       
        /*清除临时文件*/
//        clean: ['<%= meta.jsBuild %>**','<%= meta.cssCache %>**']
     });

    //注册任务
    grunt.registerTask('default', ['transport','concat','uglify']);
  };