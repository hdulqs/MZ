(function(angular) {
	angular.module("ui.materialize", [ "ui.materialize.ngModel",
			"ui.materialize.collapsible", "ui.materialize.toast",
			"ui.materialize.sidenav", "ui.materialize.material_select",
			"ui.materialize.dropdown", "ui.materialize.inputfield",
			"ui.materialize.input_date", "ui.materialize.tabs",
			"ui.materialize.pagination", "ui.materialize.pushpin",
			"ui.materialize.scrollspy", "ui.materialize.parallax",
			"ui.materialize.modal", "ui.materialize.tooltipped",
			"ui.materialize.slider", "ui.materialize.materialboxed", "ui.materialize.card" ]);
	angular
			.module("ui.materialize.ngModel", [])
			.directive(
					"ngModel",
					[
							"$timeout",
							function($timeout) {
								return {
									restrict : 'A',
									priority : -1,
									link : function(scope, element, attr) {
										scope
												.$watch(
														attr.ngModel,
														function(value) {
															$timeout(function() {
																if (value) {
																	element
																			.trigger("change");
																} else if (element
																		.attr('placeholder') === undefined) {
																	if (!element
																			.is(":focus"))
																		element
																				.trigger("blur");
																}
															});
														});
									}
								};
							} ]);
	angular
			.module("ui.materialize.slider", [])
			.directive(
					"slider",
					[
							"$timeout",
							function($timeout) {
								return {
									restrict : 'A',
									scope : {
										height : '=',
										transition : '=',
										interval : '=',
										indicators : '='
									},
									link : function(scope, element, attrs) {
										element.addClass("slider");
										$timeout(function() {
											element
													.slider({
														height : (angular
																.isDefined(scope.height)) ? scope.height
																: 400,
														transition : (angular
																.isDefined(scope.transition)) ? scope.transition
																: 500,
														interval : (angular
																.isDefined(scope.interval)) ? scope.interval
																: 6000,
														indicators : (angular
																.isDefined(scope.indicators)) ? scope.indicators
																: true
													});
										});
									}
								};
							} ]);
	angular.module("ui.materialize.collapsible", []).directive("collapsible",
			[ "$timeout", function($timeout) {
				return {
					link : function(scope, element, attrs) {
						$timeout(function() {
							element.collapsible();
						});
						if ("watch" in attrs) {
							scope.$watch(function() {
								return element[0].innerHTML;
							}, function(oldVal, newVal) {
								if (oldVal !== newVal) {
									$timeout(function() {
										element.collapsible();
									});
								}
							});
						}
					}
				};
			} ]);
	angular.module("ui.materialize.parallax", []).directive("parallax",
			[ "$timeout", function($timeout) {
				return {
					link : function(scope, element, attrs) {
						$timeout(function() {
							element.parallax();
						});
					}
				};
			} ]);
	angular
			.module("ui.materialize.toast", [])
			.constant("toastConfig", {
				duration : 3000
			})
			.directive(
					"toast",
					[
							"toastConfig",
							function(toastConfig) {
								return {
									scope : {
										message : "@",
										duration : "@",
										callback : "&"
									},
									link : function(scope, element, attrs) {
										element
												.bind(
														attrs.toast,
														function() {
															var message = (angular
																	.isDefined(scope.message)) ? scope.message
																	: "";
															var toastclass = (angular
																	.isDefined(attrs.toastclass)) ? attrs.toastclass
																	: "";
															Materialize
																	.toast(
																			message,
																			scope.duration ? scope.duration
																					: toastConfig.duration,
																			toastclass,
																			scope.callback);
														});
									}
								};
							} ]);
	angular.module('ui.materialize.pushpin', []).directive(
			'pushpin',
			[ function() {
				return {
					restrict : 'AE',
					require : [ '?pushpinTop', '?pushpinOffset',
							'?pushpinBottom' ],
					link : function(scope, element, attrs) {
						var top = attrs.pushpinTop || 0;
						var offset = attrs.pushpinOffset || 0;
						var bottom = attrs.pushpinBottom || Infinity;
						setTimeout(function() {
							element.pushpin({
								top : top,
								offset : offset,
								bottom : bottom
							});
						}, 0);
					}
				};
			} ]);
	angular.module("ui.materialize.scrollspy", []).directive("scrollspy",
			[ "$timeout", function($timeout) {
				return {
					restrict : 'A',
					link : function(scope, element, attrs) {
						element.addClass("scrollspy");
						$timeout(function() {
							element.scrollSpy();
						});
					}
				};
			} ]);
	angular.module("ui.materialize.tabs", []).directive("tabs", [ function() {
		return {
			link : function(scope, element, attrs) {
				element.tabs();
			}
		};
	} ]);
	angular
			.module("ui.materialize.sidenav", [])
			.directive(
					"sidenav",
					[ function() {
						return {
							scope : {
								menuwidth : "@",
								closeonclick : "@"
							},
							link : function(scope, element, attrs) {
								element
										.sideNav({
											menuWidth : (angular
													.isDefined(scope.menuwidth)) ? scope.menuwidth
													: undefined,
											edge : attrs.sidenav ? attrs.sidenav
													: "left",
											closeOnClick : (angular
													.isDefined(scope.closeonclick)) ? scope.closeonclick == "true"
													: undefined
										});
							}
						};
					} ]);
	angular.module("ui.materialize.material_select", []).directive(
			"materialSelect",
			[ "$compile", "$timeout", function($compile, $timeout) {
				return {
					link : function(scope, element, attrs) {
						if (element.is("select")) {
							$compile(element.contents())(scope);
							function initSelect() {
								element.siblings(".caret").remove();
								element.material_select();
							}
							$timeout(initSelect);
							if (attrs.ngModel) {
								scope.$watch(attrs.ngModel, initSelect);
							}
							if ("watch" in attrs) {
								scope.$watch(function() {
									return element[0].innerHTML;
								}, function(oldVal, newVal) {
									if (oldVal !== newVal) {
										$timeout(initSelect);
									}
								});
							}
						}
					}
				};
			} ]);
	angular
			.module("ui.materialize.dropdown", [])
			.directive(
					"dropdown",
					[
							"$compile",
							"$timeout",
							function($compile, $timeout) {
								return {
									scope : {
										inDuration : "@",
										outDuration : "@",
										constrainWidth : "@",
										hover : "@",
										alignment : "@",
										gutter : "@",
										belowOrigin : "@"
									},
									link : function(scope, element, attrs) {
										$timeout(function() {
											$compile(element.contents())(scope);
											element
													.dropdown({
														inDuration : (angular
																.isDefined(scope.inDuration)) ? scope.inDuration
																: undefined,
														outDuration : (angular
																.isDefined(scope.outDuration)) ? scope.outDuration
																: undefined,
														constrain_width : (angular
																.isDefined(scope.constrainWidth)) ? scope.constrainWidth
																: undefined,
														hover : (angular
																.isDefined(scope.hover)) ? scope.hover
																: undefined,
														alignment : (angular
																.isDefined(scope.alignment)) ? scope.alignment
																: undefined,
														gutter : (angular
																.isDefined(scope.gutter)) ? scope.gutter
																: undefined,
														belowOrigin : (angular
																.isDefined(scope.belowOrigin)) ? scope.belowOrigin
																: undefined
													});
										});
									}
								};
							} ]);
	angular
			.module("ui.materialize.inputfield", [])
			.directive(
					'inputField',
					[
							"$compile",
							"$timeout",
							function($compile, $timeout) {
								return {
									transclude : true,
									scope : {},
									link : function(scope, element) {
										$timeout(function() {
											Materialize.updateTextFields();
											element
													.find(
															"> > .materialize-textarea")
													.each(
															function() {
																var that = $(this);
																that
																		.addClass("materialize-textarea");
																that
																		.trigger("autoresize");
																var model = that
																		.attr("ng-model");
																if (model) {
																	scope.$parent
																			.$watch(
																					model,
																					function(
																							a,
																							b) {
																						if (a !== b) {
																							$timeout(function() {
																								that
																										.trigger("autoresize");
																							});
																						}
																					});
																}
															});
											element
													.find(
															'> > .materialize-textarea, > > input')
													.each(
															function(index,
																	countable) {
																countable = angular
																		.element(countable);
																if (!countable
																		.siblings('span[class="character-counter"]').length) {
																	countable
																			.characterCounter();
																}
															});
										});
									},
									template : '<div ng-transclude class="input-field"></div>'
								};
							} ]);
	angular
			.module("ui.materialize.input_date", [])
			.directive(
					'inputDate',
					[
							"$compile",
							"$timeout",
							function($compile, $timeout) {
								var style = $('<style>#inputCreated_root {outline: none;}</style>');
								$('html > head').append(style);
								var dateFormat = function() {
									var token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g, timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g, timezoneClip = /[^-+\dA-Z]/g, pad = function(
											val, len) {
										val = String(val);
										len = len || 2;
										while (val.length < len) {
											val = "0" + val;
										}
										return val;
									};
									return function(date, mask, utc) {
										var dF = dateFormat;
										if (arguments.length === 1
												&& Object.prototype.toString
														.call(date) == "[object String]"
												&& !/\d/.test(date)) {
											mask = date;
											date = undefined;
										}
										date = date ? new Date(date)
												: new Date();
										if (isNaN(date))
											throw SyntaxError("invalid date");
										mask = String(dF.masks[mask] || mask
												|| dF.masks["default"]);
										if (mask.slice(0, 4) == "UTC:") {
											mask = mask.slice(4);
											utc = true;
										}
										var _ = utc ? "getUTC" : "get", d = date[_
												+ "Date"](), D = date[_ + "Day"]
												(), m = date[_ + "Month"](), y = date[_
												+ "FullYear"](), H = date[_
												+ "Hours"](), M = date[_
												+ "Minutes"](), s = date[_
												+ "Seconds"](), L = date[_
												+ "Milliseconds"](), o = utc ? 0
												: date.getTimezoneOffset(), flags = {
											d : d,
											dd : pad(d),
											ddd : dF.i18n.dayNames[D],
											dddd : dF.i18n.dayNames[D + 7],
											m : m + 1,
											mm : pad(m + 1),
											mmm : dF.i18n.monthNames[m],
											mmmm : dF.i18n.monthNames[m + 12],
											yy : String(y).slice(2),
											yyyy : y,
											h : H % 12 || 12,
											hh : pad(H % 12 || 12),
											H : H,
											HH : pad(H),
											M : M,
											MM : pad(M),
											s : s,
											ss : pad(s),
											l : pad(L, 3),
											L : pad(L > 99 ? Math.round(L / 10)
													: L),
											t : H < 12 ? "a" : "p",
											tt : H < 12 ? "am" : "pm",
											T : H < 12 ? "A" : "P",
											TT : H < 12 ? "AM" : "PM",
											Z : utc ? "UTC" : (String(date)
													.match(timezone) || [ "" ])
													.pop().replace(
															timezoneClip, ""),
											o : (o > 0 ? "-" : "+")
													+ pad(Math.floor(Math
															.abs(o) / 60)
															* 100
															+ Math.abs(o)
															% 60, 4),
											S : [ "th", "st", "nd", "rd" ][d % 10 > 3 ? 0
													: (d % 100 - d % 10 != 10)
															* d % 10]
										};
										return mask
												.replace(
														token,
														function($0) {
															return $0 in flags ? flags[$0]
																	: $0
																			.slice(
																					1,
																					$0.length - 1);
														});
									};
								}();
								dateFormat.masks = {
									"default" : "ddd mmm dd yyyy HH:MM:ss",
									shortDate : "m/d/yy",
									mediumDate : "mmm d, yyyy",
									longDate : "mmmm d, yyyy",
									fullDate : "dddd, mmmm d, yyyy",
									shortTime : "h:MM TT",
									mediumTime : "h:MM:ss TT",
									longTime : "h:MM:ss TT Z",
									isoDate : "yyyy-mm-dd",
									isoTime : "HH:MM:ss",
									isoDateTime : "yyyy-mm-dd'T'HH:MM:ss",
									isoUtcDateTime : "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
								};
								dateFormat.i18n = {
									dayNames : [ "Sun", "Mon", "Tue", "Wed",
											"Thu", "Fri", "Sat", "Sunday",
											"Monday", "Tuesday", "Wednesday",
											"Thursday", "Friday", "Saturday" ],
									monthNames : [ "Jan", "Feb", "Mar", "Apr",
											"May", "Jun", "Jul", "Aug", "Sep",
											"Oct", "Nov", "Dec", "January",
											"February", "March", "April",
											"May", "June", "July", "August",
											"September", "October", "November",
											"December" ]
								};
								Date.prototype.format = function(mask, utc) {
									return dateFormat(this, mask, utc);
								};
								var isValidDate = function(date) {
									if (Object.prototype.toString.call(date) === '[object Date]') {
										return !isNaN(date.getTime());
									}
									return false;
								};
								return {
									require : 'ngModel',
									scope : {
										container : "@",
										format : "@",
										formatSubmit : "@",
										monthsFull : "@",
										monthsShort : "@",
										weekdaysFull : "@",
										weekdaysLetter : "@",
										firstDay : "=",
										disable : "=",
										today : "=",
										clear : "=",
										close : "=",
										selectYears : "=",
										onStart : "&",
										onRender : "&",
										onOpen : "&",
										onClose : "&",
										onSet : "&",
										onStop : "&",
										ngReadonly : "=?",
										max : "@",
										min : "@"
									},
									link : function(scope, element, attrs,
											ngModelCtrl) {
										ngModelCtrl.$formatters
												.unshift(function(modelValue) {
													if (modelValue) {
														var date = new Date(
																modelValue);
														return (angular
																.isDefined(scope.format)) ? date
																.format(scope.format)
																: date
																		.format('d mmmm, yyyy');
													}
													return null;
												});
										var monthsFull = (angular
												.isDefined(scope.monthsFull)) ? scope
												.$eval(scope.monthsFull)
												: undefined, monthsShort = (angular
												.isDefined(scope.monthsShort)) ? scope
												.$eval(scope.monthsShort)
												: undefined, weekdaysFull = (angular
												.isDefined(scope.weekdaysFull)) ? scope
												.$eval(scope.weekdaysFull)
												: undefined, weekdaysLetter = (angular
												.isDefined(scope.weekdaysLetter)) ? scope
												.$eval(scope.weekdaysLetter)
												: undefined;
										$compile(element.contents())(scope);
										if (!(scope.ngReadonly)) {
											$timeout(function() {
												var pickadateInput = element
														.pickadate({
															container : (angular
																	.isDefined(scope.container)) ? scope.container
																	: 'body',
															format : (angular
																	.isDefined(scope.format)) ? scope.format
																	: undefined,
															formatSubmit : (angular
																	.isDefined(scope.formatSubmit)) ? scope.formatSubmit
																	: undefined,
															monthsFull : (angular
																	.isDefined(monthsFull)) ? monthsFull
																	: undefined,
															monthsShort : (angular
																	.isDefined(monthsShort)) ? monthsShort
																	: undefined,
															weekdaysFull : (angular
																	.isDefined(weekdaysFull)) ? weekdaysFull
																	: undefined,
															weekdaysLetter : (angular
																	.isDefined(weekdaysLetter)) ? weekdaysLetter
																	: undefined,
															firstDay : (angular
																	.isDefined(scope.firstDay)) ? scope.firstDay
																	: 0,
															disable : (angular
																	.isDefined(scope.disable)) ? scope.disable
																	: undefined,
															today : (angular
																	.isDefined(scope.today)) ? scope.today
																	: undefined,
															clear : (angular
																	.isDefined(scope.clear)) ? scope.clear
																	: undefined,
															close : (angular
																	.isDefined(scope.close)) ? scope.close
																	: undefined,
															selectYears : (angular
																	.isDefined(scope.selectYears)) ? scope.selectYears
																	: undefined,
															onStart : (angular
																	.isDefined(scope.onStart)) ? function() {
																scope.onStart();
															}
																	: undefined,
															onRender : (angular
																	.isDefined(scope.onRender)) ? function() {
																scope
																		.onRender();
															}
																	: undefined,
															onOpen : (angular
																	.isDefined(scope.onOpen)) ? function() {
																scope.onOpen();
															}
																	: undefined,
															onClose : (angular
																	.isDefined(scope.onClose)) ? function() {
																scope.onClose();
															}
																	: undefined,
															onSet : (angular
																	.isDefined(scope.onSet)) ? function() {
																scope.onSet();
															}
																	: undefined,
															onStop : (angular
																	.isDefined(scope.onStop)) ? function() {
																scope.onStop();
															}
																	: undefined
														});
												var picker = pickadateInput
														.pickadate('picker');
												scope
														.$watch(
																'max',
																function(newMax) {
																	if (picker) {
																		var maxDate = new Date(
																				newMax);
																		picker
																				.set({
																					max : isValidDate(maxDate) ? maxDate
																							: false
																				});
																	}
																});
												scope
														.$watch(
																'min',
																function(newMin) {
																	if (picker) {
																		var minDate = new Date(
																				newMin);
																		picker
																				.set({
																					min : isValidDate(minDate) ? minDate
																							: false
																				});
																	}
																});
											});
										}
									}
								};
							} ]);
	angular
			.module("ui.materialize.pagination", [])
			.directive(
					'pagination',
					function() {
						function setScopeValues(scope, attrs) {
							scope.List = [];
							scope.Hide = false;
							scope.page = parseInt(scope.page) || 1;
							scope.total = parseInt(scope.total) || 0;
							scope.dots = scope.dots || '...';
							scope.ulClass = scope.ulClass || attrs.ulClass
									|| 'pagination';
							scope.adjacent = parseInt(scope.adjacent) || 2;
							scope.activeClass = 'active';
							scope.disabledClass = 'disabled';
							scope.scrollTop = scope.$eval(attrs.scrollTop);
							scope.hideIfEmpty = scope.$eval(attrs.hideIfEmpty);
							scope.showPrevNext = scope
									.$eval(attrs.showPrevNext);
						}
						function validateScopeValues(scope, pageCount) {
							if (scope.page > pageCount) {
								scope.page = pageCount;
							}
							if (scope.page <= 0) {
								scope.page = 1;
							}
							if (scope.adjacent <= 0) {
								scope.adjacent = 2;
							}
							if (pageCount <= 1) {
								scope.Hide = scope.hideIfEmpty;
							}
						}
						function internalAction(scope, page) {
							if (scope.page == page) {
								return;
							}
							scope.page = page;
							scope.paginationAction({
								page : page
							});
							if (scope.scrollTop) {
								scrollTo(0, 0);
							}
						}
						function addRange(start, finish, scope) {
							var i = 0;
							for (i = start; i <= finish; i++) {
								var item = {
									value : i.toString(),
									liClass : scope.page == i ? scope.activeClass
											: 'waves-effect',
									action : function() {
										internalAction(scope, this.value);
									}
								};
								scope.List.push(item);
							}
						}
						function addDots(scope) {
							scope.List.push({
								value : scope.dots
							});
						}
						function addFirst(scope, next) {
							addRange(1, 2, scope);
							if (next != 3) {
								addDots(scope);
							}
						}
						function addPrevNext(scope, pageCount, mode) {
							if (!scope.showPrevNext || pageCount < 1) {
								return;
							}
							var disabled, alpha, beta;
							if (mode === 'prev') {
								disabled = scope.page - 1 <= 0;
								var prevPage = scope.page - 1 <= 0 ? 1
										: scope.page - 1;
								alpha = {
									value : "<<",
									title : 'First Page',
									page : 1
								};
								beta = {
									value : "<",
									title : 'Previous Page',
									page : prevPage
								};
							} else {
								disabled = scope.page + 1 > pageCount;
								var nextPage = scope.page + 1 >= pageCount ? pageCount
										: scope.page + 1;
								alpha = {
									value : ">",
									title : 'Next Page',
									page : nextPage
								};
								beta = {
									value : ">>",
									title : 'Last Page',
									page : pageCount
								};
							}
							var addItem = function(item, disabled) {
								scope.List.push({
									value : item.value,
									title : item.title,
									liClass : disabled ? scope.disabledClass
											: '',
									action : function() {
										if (!disabled) {
											internalAction(scope, item.page);
										}
									}
								});
							};
							addItem(alpha, disabled);
							addItem(beta, disabled);
						}
						function addLast(pageCount, scope, prev) {
							if (prev != pageCount - 2) {
								addDots(scope);
							}
							addRange(pageCount - 1, pageCount, scope);
						}
						function build(scope, attrs) {
							if (!scope.pageSize || scope.pageSize < 0) {
								return;
							}
							setScopeValues(scope, attrs);
							var start, size = scope.adjacent * 2, pageCount = Math
									.ceil(scope.total / scope.pageSize);
							validateScopeValues(scope, pageCount);
							addPrevNext(scope, pageCount, 'prev');
							if (pageCount < (5 + size)) {
								start = 1;
								addRange(start, pageCount, scope);
							} else {
								var finish;
								if (scope.page <= (1 + size)) {
									start = 1;
									finish = 2 + size + (scope.adjacent - 1);
									addRange(start, finish, scope);
									addLast(pageCount, scope, finish);
								} else if (pageCount - size > scope.page
										&& scope.page > size) {
									start = scope.page - scope.adjacent;
									finish = scope.page + scope.adjacent;
									addFirst(scope, start);
									addRange(start, finish, scope);
									addLast(pageCount, scope, finish);
								} else {
									start = pageCount
											- (1 + size + (scope.adjacent - 1));
									finish = pageCount;
									addFirst(scope, start);
									addRange(start, finish, scope);
								}
							}
							addPrevNext(scope, pageCount, 'next');
						}
						return {
							restrict : 'EA',
							scope : {
								page : '=',
								pageSize : '=',
								total : '=',
								dots : '@',
								hideIfEmpty : '@',
								adjacent : '@',
								scrollTop : '@',
								showPrevNext : '@',
								paginationAction : '&',
								ulClass : '=?'
							},
							template : '<ul ng-hide="Hide" ng-class="ulClass"> '
									+ '<li '
									+ 'ng-class="Item.liClass" '
									+ 'ng-click="Item.action()" '
									+ 'ng-repeat="Item in List"> '
									+ '<a href> '
									+ '<span ng-bind="Item.value"></span> '
									+ '</a>' + '</ul>',
							link : function(scope, element, attrs) {
								scope.$watchCollection(
										'[page, total, pageSize]', function() {
											build(scope, attrs);
										});
							}
						};
					});
	angular
			.module("ui.materialize.modal", [])
			.directive(
					"modal",
					[
							"$compile",
							"$timeout",
							function($compile, $timeout) {
								return {
									scope : {
										dismissible : "=",
										opacity : "@",
										inDuration : "@",
										outDuration : "@"
									},
									link : function(scope, element, attrs) {
										$timeout(function() {
											$compile(element.contents())(scope);
											element
													.leanModal({
														dismissible : (angular
																.isDefined(scope.dismissible)) ? scope.dismissible
																: undefined,
														opacity : (angular
																.isDefined(scope.opacity)) ? scope.opacity
																: undefined,
														in_duration : (angular
																.isDefined(scope.inDuration)) ? scope.inDuration
																: undefined,
														out_duration : (angular
																.isDefined(scope.outDuration)) ? scope.outDuration
																: undefined
													});
										});
									}
								};
							} ]);
	angular.module("ui.materialize.tooltipped", []).directive("tooltipped",
			[ "$compile", "$timeout", function($compile, $timeout) {
				return {
					restrict : "EA",
					scope : true,
					link : function(scope, element, attrs) {
						element.addClass("tooltipped");
						$compile(element.contents())(scope);
						$timeout(function() {
							element.tooltip();
						});
						scope.$on('$destroy', function() {
							element.tooltip("remove");
						});
					}
				};
			} ]);
	angular.module("ui.materialize.materialboxed", []).directive(
			"materialboxed", [ "$timeout", function($timeout) {
				return {
					restrict : 'A',
					link : function(scope, element, attrs) {
						$timeout(function() {
							element.materialbox();
						});
					}
				};
			} ]);
	
	
	angular.module("ui.materialize.card", []).directive(
			"card", [ "$timeout", function($timeout) {
				return {
					restrict : 'C',
					link : function(scope, element, attrs) {
						  var Card = function(element, options) {
						    this.options      = options;
						    this.$card        = $(element);
						    this.$closeBtn    = this.$card.find('> .title > .close');
						    this.$minimizeBtn = this.$card.find('> .title > .minimize');
						    this.$content     = this.$card.find('> .content');
						    this.$window      = $(window);
						  };

						  Card.DEFAULTS = {
						    // duration of all animations
						    duration: 300
						  };

						  // init card
						  Card.prototype.init = function() {
						    var _this = this;

						    // Remove card
						    _this.$closeBtn.on('click', function(e) {
						      e.preventDefault();
						      _this.close();
						    });

						    // Minimize card
						    _this.$minimizeBtn.on('click', function(e) {
						    	debugger
						      e.preventDefault();
						      _this.minimize();
						    });
						  }

						  // cloase card
						  Card.prototype.close = function() {
						    var _this = this;

						    // remove animation
						    _this.$card.velocity({
						      opacity: 0,
						      translateY: -20
						    }, _this.options.duration )
						    
						    .velocity('slideUp', _this.options.duration, function() {
						      _this.$card.remove();
						    });
						  }

						  // minimize card
						  Card.prototype.minimize = function() {
						    var _this = this;

						    if(_this.$card.hasClass('minimized')) {
						      _this.$content
						        .css('display', 'none')
						        .velocity('slideDown', 'swing', _this.options.duration);
						    } else {
						      _this.$content
						        .css('display', 'block')
						        .velocity('slideUp', 'swing', _this.options.duration);
						    }

						    _this.$card.toggleClass('minimized');

						    // resize for nano scroller and charts
						    _this.$window.resize();
						  }

						  // init
						
						    $('.card').each(function() {
						      var options = $.extend({}, Card.DEFAULTS, $(this).data(), typeof option == 'object' && option);
						      var curCard = new Card(this, options);
						      // call init
						      curCard.init();
						    });
						 
						  
						  
					}
				};
			} ]);
	
}(angular));