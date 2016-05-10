

YUI.add('birthday-portlet', function (Y, NAME) {
    
    Y.Handlebars.registerHelper('formatISODate', function(isoDate) {
        var date = new Date(isoDate);
        var str = Y.Date.format(date, {format:"%A, %b %d"});
        return new Y.Handlebars.SafeString(str);
    })
    
    var getTemplate = function(id) {
        var templateString = Y.one(id).get('innerHTML');
        return Y.Handlebars.compile(templateString);
    };
    
    var getStartWeekDate = function(date) {
        var day = date.getDay();
        var diff = date.getDate() - day + (day == 0 ? -6:1); // adjust when day is sunday
        return new Date(date.setDate(diff));
    };
    
    var getEndWeekDate = function(date) {
        var start = getStartWeekDate(date);
        return Y.Date.addDays(start, 6);
    };
    
    function MonthDataFunctions(){
    	return {
    		saveMonthData : function(self, monthData){
    			self.set('currentMonthData', monthData);
    		},
    		getDateData : function(self, dateStr){
    	    	var daysArr = self.get('currentMonthData').dates;
    	    	for(var i = 0; i < daysArr.length; i++){
    	    		if(dateStr == daysArr[i].dateStr){
    	    			return daysArr[i];
    	    		}
    	    	}
    	    	return null;
    	    },
    	    setOpened : function(self, dateStr, opened){
    	    	var monthData = self.get('currentMonthData');
    	    	var daysArr = monthData.dates;
    	    	for(var i = 0; i < daysArr.length; i++){
    	    		if(dateStr == daysArr[i].dateStr){
    	    			daysArr[i].opened = opened;
    	    			monthData.dates = daysArr;
    	    			self.set('currentMonthData', monthData);
    	    			return;
    	    		}
    	    	}
    	    },
    	    setEmpty : function(self, dateStr, empty){
    	    	var monthData = self.get('currentMonthData');
    	    	var daysArr = monthData.dates;
    	    	for(var i = 0; i < daysArr.length; i++){
    	    		if(dateStr == daysArr[i].dateStr){
    	    			daysArr[i].empty = empty;
    	    			monthData.dates = daysArr;
    	    			self.set('currentMonthData', monthData);
    	    			return;
    	    		}
    	    	}
    	    },
    	    setDateData : function(self, data){
    	    	if(self && data){
    	    		var monthData = self.get('currentMonthData');
    	    		var daysArr = monthData.dates;
    	    		var arr = [];
    	    		for(var i = 0; i < daysArr.length; i++){
    	    			if(daysArr[i].dateStr == data.dateStr){
    	    				arr.push(data);
    	    			}else{
    	    				arr.push(daysArr[i]);
    	    			}
    	    		}
    	    		monthData.dates = arr;
    	    		self.set('currentMonthData', monthData);
    	    	}
    	    },
    	    addDateData : function(self, data){
    	    	if(self && data){
    				var monthData = self.get('currentMonthData');
    				var daysArr = monthData.dates;
    				daysArr.push(data);
    	    		monthData.dates = daysArr;
    	    		self.set('currentMonthData', monthData);
    	    	}
    	    },
    	    addDatesData : function(self, dataArr){
    	    	if(!dataArr || !self)return;
    	    	var monthData = self.get('currentMonthData');
    	    	for(var i = 0; i < dataArr.length; i++){
    	    		if(null != MonthFunctions.getDateData(self, dataArr[i].dateStr)){
    	    			this.setDateData(self, dataArr[i]);
    	    		}else{
    	    			this.addDateData(self, dataArr[i]);
    	    		}
    	    	}
    	    },
    	    checkPreviousOpenedDates : function(self){
    	        Y.all('.grouped-dates-wrapper').each(function (wrapperNode) {
    	        	var dateStr = wrapperNode.one('.toggle').getData('dateStr');
    	        	var daysArr = self.get('currentMonthData').dates;
    	        	if(daysArr){
    	            	for(var i = 0; i < daysArr.length; i++){
    	            		if(dateStr == daysArr[i].dateStr && daysArr[i].opened == true){
    	                    	wrapperNode.toggleClass('opened');
    	                    	wrapperNode.one('.content').toggleClass('hidden');
    	                    	wrapperNode.one('.toggle').one('.toggle-icon').toggleClass('icon-plus').toggleClass('icon-minus');
    	            		}
    	            	}
    	        	}
    	        });
    	    }
    	}
    }
    
    var MonthFunctions = new MonthDataFunctions();
    
    var TEMPLATES = {
        bigInfoDateTemplate: getTemplate('#bday-big-date'),
        dailyListTemplate: getTemplate('#bday-daily-list'),
        footerTemplate: getTemplate('#bday-footer'),
        weekTemplate: getTemplate('#bday-week'),
        monthTemplate: getTemplate('#bday-month'),
        groupedListTemplate: getTemplate('#bday-grouped-list')
    };
    
    var CONTAINER = '.portlet-body';
    var VIEW_CONTAINER = '.bday-view-container';
    var SHOW_MORE = ".bday-show-more";
    
    var BirthdayPortlet = Y.Base.create('birthday-portlet', Y.Base, [Liferay.PortletBase], {
        
        initializer: function () {
            this.renderUI();
            this.bindUI();
        },
        
        /**
         *
         * Attaches elements events
         *
         *
         */
        bindUI: function() {
            var self = this;
            
            this.after('currentDateChange', function() {
                self.renderUI();
            });
            this.after('currentMonthChange', function() {
                self.renderUI();
            });
            this.after('currentWeekChange', function() {
                self.renderUI();
            });
            this.after('viewModeChange', function() {
                self.one('.view-mode').set('value', self.get('viewMode'));
            });
            // view mode select
            this.one('.view-mode').on('change', function(e) {
                self.set('viewMode', this.get('value'));
                self.renderUI();
            });
            
            // click to go to prev date
            this.one('.pager .previous').on('click', function(e) {
                e.preventDefault();
                self.prev();
            });
            
            // click to go to next date
            this.one('.pager .next').on('click', function(e) {
                e.preventDefault();
                self.next();
            });
            
            // for monthly and weekly, day expand/collapse
            this.one(VIEW_CONTAINER).delegate('click', function(e) {
            	
                e.preventDefault()
                var toggleIcon = this.one('.toggle-icon');
                var wrapper = this.ancestor('.wrapper');
                
                if(BirthdayPortlet.CONSTANTS.Weekly == self.get('viewMode')){
                    wrapper.toggleClass('opened');
                    wrapper.one('.content').toggleClass('hidden');
                    toggleIcon.toggleClass('icon-plus').toggleClass('icon-minus');
                }
                else{
	                
	            	var currentMonth = self.get('currentMonth');
	            	var currentMonthData = self.get('currentMonthData');
	                var currentDate = currentMonth.date;
	                var dateStr = this.getData('dateStr');
	            	var currentDay = new Date(dateStr).getDate();
	                
	                if(!wrapper.hasClass('opened')){//If is an open event
	                	
		                var data = Liferay.Util.ns(self.NS, {
		                    year: currentDate.getFullYear(),
		                    month: currentDate.getMonth() + 1,
		                    timeZoneOffset: currentDate.getTimezoneOffset(),
		                    type: BirthdayPortlet.CONSTANTS.Days,
		                    day: currentDay
		                });
		                var dayData = MonthFunctions.getDateData(self, dateStr);
		                if(dayData && !dayData.empty){	//day is already in map	                	
		                	var footer = {empty: false, viewMode: self.get('viewMode'), upcomingDate: null};
		                    var dates = {
		                            month: Y.Date.format(self.get('currentMonth').date, {format:"%B %Y"})
		                    }
		                    self.one(VIEW_CONTAINER).empty();
		                    self.one(VIEW_CONTAINER).append(TEMPLATES.monthTemplate(dates));
		                    self.one(VIEW_CONTAINER).append(TEMPLATES.groupedListTemplate(currentMonthData));
		                    self.one(VIEW_CONTAINER).append(TEMPLATES.footerTemplate(footer));
	
		                    MonthFunctions.setOpened(self, dateStr, true);
		                    MonthFunctions.checkPreviousOpenedDates(self);
		                }else{
		                	
		                    var dataString = '';
		                    for (var key in data) {
		                	   if (data.hasOwnProperty(key)) {
		                	      dataString += data[key]+'-';
		                	   }
		                    }
		                    self.set('lastRequestStr', dataString);
		                	
			                self.requestData('', data, function(data) {
			                	
			                    if (self.get('lastRequestStr') !== dataString) {
			                        return;
			                    }
			                	
			                    var footer = {empty: false, viewMode: self.get('viewMode'), upcomingDate: null};
			                    var dates = {
			                            month: Y.Date.format(self.get('currentMonth').date, {format:"%B %Y"})
			                        }
			                    
			                    MonthFunctions.addDatesData(self, data.dates);
			                    
			                    var monthData = self.get('currentMonthData');
			                    
			                    self.one(VIEW_CONTAINER).empty();
			                    self.one(VIEW_CONTAINER).append(TEMPLATES.monthTemplate(dates));
			                    self.one(VIEW_CONTAINER).append(TEMPLATES.groupedListTemplate(monthData));
			                    self.one(VIEW_CONTAINER).append(TEMPLATES.footerTemplate(footer));
				                
			                    MonthFunctions.setOpened(self, dateStr, true);
			                    MonthFunctions.setEmpty(self, dateStr, false);
			                    MonthFunctions.checkPreviousOpenedDates(self);
			                });
		                }
	                }
	                else{
	                	MonthFunctions.setOpened(self, dateStr, false);
	                    var toggleIcon = this.one('.toggle-icon');
	                    var wrapper = this.ancestor('.wrapper');
	                    wrapper.toggleClass('opened');
	                    wrapper.one('.content').toggleClass('hidden');
	                    toggleIcon.toggleClass('icon-plus').toggleClass('icon-minus');
	                }
                }
                
            }, '.toggle');
            
            // go to upcoming day
            this.one(VIEW_CONTAINER).delegate('click', function(e) {
                e.preventDefault();
                var date = new Date(this.getAttribute('data-upcoming'));
                self.set('viewMode', BirthdayPortlet.CONSTANTS.Daily);
                self.set('currentDate', date);
            }, '.upcoming');
            
            // go back to today
            this.one('.btn-back').on('click', function(e) {
                e.preventDefault();
                var date = new Date();
                self.set('viewMode', BirthdayPortlet.CONSTANTS.Daily);
                self.set('currentDate', date);
            });
            
        },
        
        /**
         *
         * Renders UI and knows what view mode is active
         *
         *
         */
        renderUI: function() {
            this.one(VIEW_CONTAINER).empty();
            this.one('.btn-back').removeClass('hidden').addClass('show');
            switch (this.get('viewMode')) {
                case BirthdayPortlet.CONSTANTS.Daily:
                    this.renderDailyUI();
                    break;
                case BirthdayPortlet.CONSTANTS.Weekly:
                    this.renderWeekUI();
                    break;
                case BirthdayPortlet.CONSTANTS.Monthly:
                    this.renderMonthUI();
                    break;
                default:
                    this.renderDailyUI();
            }
        },
        
        /**
         *
         * Renders birthdays per day
         *
         *
         */
        renderDailyUI: function() {
            var self = this;
            var isToday = new Date().toDateString() === this.get('currentDate').toDateString();
            var weekDay = isToday ? 'Today' : Y.Date.format(this.get('currentDate'), {format:"%A"});
            var dates = {
                day: Y.Date.format(this.get('currentDate'), {format:"%e"}),
                weekDay: weekDay,
                fullDate: Y.Date.format(this.get('currentDate'), {format:"%B %d, %Y"})
            }
            this.one(VIEW_CONTAINER).append(TEMPLATES.bigInfoDateTemplate(dates));
            
            var currentDate = this.get('currentDate');
            var data = Liferay.Util.ns(this.NS, {
                year: currentDate.getFullYear(),
                day: currentDate.getDate(),
                month: currentDate.getMonth() + 1,
                timeZoneOffset: currentDate.getTimezoneOffset(),
                type: BirthdayPortlet.CONSTANTS.Daily
            });
            
            var dataString = '';
            for (var key in data) {
        	   if (data.hasOwnProperty(key)) {
        	      dataString += data[key]+'-';
        	   }
            }
            self.set('lastRequestStr', dataString);
            
            this.requestData('', data, function(data) {
            	
                if (self.get('lastRequestStr') !== dataString) {
                    return;
                }
            	
                var footer = {empty: (!data.users || data.users.length == 0), viewMode: self.get('viewMode'), upcomingDate: data.upcomingDate};
                self.one(VIEW_CONTAINER).append(TEMPLATES.dailyListTemplate(data));
                self.one(VIEW_CONTAINER).append(TEMPLATES.footerTemplate(footer));
            });
            
            if(isToday){
            	this.one('.btn-back').removeClass('show').addClass('hidden');
            }
            
        },
        
        /**
         *
         * Renders birthdays per week
         *
         *
         */
        renderWeekUI: function() {
            var self = this;
            var dates = {
                start: Y.Date.format(this.get('currentWeek').start, {format:"%B %e, %Y"}),
                end: Y.Date.format(this.get('currentWeek').end, {format:"%B %e, %Y"})
            }
            this.one(VIEW_CONTAINER).append(TEMPLATES.weekTemplate(dates));
            var data = Liferay.Util.ns(this.NS, {
                year1: this.get('currentWeek').start.getFullYear(),
                day1: this.get('currentWeek').start.getDate(),
                month1: this.get('currentWeek').start.getMonth() + 1,
                year2: this.get('currentWeek').end.getFullYear(),
                day2: this.get('currentWeek').end.getDate(),
                month2: this.get('currentWeek').end.getMonth() + 1,
                timeZoneOffset: this.get('currentWeek').start.getTimezoneOffset(),
                type: BirthdayPortlet.CONSTANTS.Weekly
            });
            
            var dataString = '';
            for (var key in data) {
        	   if (data.hasOwnProperty(key)) {
        	      dataString += data[key]+'-';
        	   }
            }
            self.set('lastRequestStr', dataString);
            
            this.requestData('', data, function(data) {
            	
                if (self.get('lastRequestStr') !== dataString) {
                    return;
                }
            	
                var footer = {empty: (!data.dates || data.dates.length == 0), viewMode: self.get('viewMode'), upcomingDate: data.upcomingDate};
                self.one(VIEW_CONTAINER).append(TEMPLATES.groupedListTemplate(data));
                self.one(VIEW_CONTAINER).append(TEMPLATES.footerTemplate(footer));
            });
        },
        
        /**
         *
         * Renders birthdays per month
         *
         *
         */
        renderMonthUI: function() {
            var self = this;
            var dates = {
                month: Y.Date.format(this.get('currentMonth').date, {format:"%B %Y"})
            }
            this.one(VIEW_CONTAINER).append(TEMPLATES.monthTemplate(dates));
            var currentDate = this.get('currentMonth').date;
            var data = Liferay.Util.ns(this.NS, {
                year: currentDate.getFullYear(),
                month: currentDate.getMonth() + 1,
                timeZoneOffset: currentDate.getTimezoneOffset(),
                type: BirthdayPortlet.CONSTANTS.Monthly
            });

            var dataString = '';
            for (var key in data) {
        	   if (data.hasOwnProperty(key)) {
        	      dataString += data[key]+'-';
        	   }
            }

            self.set('lastRequestStr', dataString);

            this.requestData('', data, function(data) {
            	
                if (self.get('lastRequestStr') !== dataString) {
                    return;
                }
                
            	var isEmpty = (!data.dates || data.dates.length == 0);
            	if(!isEmpty){
            		var monthData = self.get('currentMonthData');
            		var arr = [];
            		var dates = data.dates;
            		for(var i = 0; i< dates.length; i++){
            			var dateData = dates[i];
            			dateData.empty = true;
            			arr.push(dateData);
            		}
            		monthData.dates = arr;
            		MonthFunctions.saveMonthData(self, monthData);
            	}
                var footer = {empty: isEmpty, viewMode: self.get('viewMode'), upcomingDate: data.upcomingDate};
                
                self.one(VIEW_CONTAINER).append(TEMPLATES.groupedListTemplate(data));
                self.one(VIEW_CONTAINER).append(TEMPLATES.footerTemplate(footer));
            });
        },
        
        /**
         *
         * Set next day, week or month
         *
         *
         */
        next: function() {
            switch (this.get('viewMode')) {
                case BirthdayPortlet.CONSTANTS.Daily:
                    this.set('currentDate', Y.Date.addDays(this.get('currentDate'), 1));
                    break;
                case BirthdayPortlet.CONSTANTS.Weekly:
                    var week = this.get('currentWeek');
                    week.start = Y.Date.addDays(week.start, 7);
                    week.end = Y.Date.addDays(week.start, 6);
                    this.set('currentWeek', week);
                    break;
                case BirthdayPortlet.CONSTANTS.Monthly:
                	var month = this.get('currentMonth');
                	month.date = Y.Date.addMonths(month.date, 1);
                    this.set('currentMonth', month);
                    break;
                default:
                    this.set('currentDate', Y.Date.addDays(this.get('currentDate'), 1));
            }
        },
        
        /**
         *
         * Set previous day, week or month
         *
         *
         */
        prev: function() {
            switch (this.get('viewMode')) {
                case BirthdayPortlet.CONSTANTS.Daily:
                    this.set('currentDate', Y.Date.addDays(this.get('currentDate'), -1));
                    break;
                case BirthdayPortlet.CONSTANTS.Weekly:
                    var week = this.get('currentWeek');
                    week.start = Y.Date.addDays(week.start, -7);
                    week.end = Y.Date.addDays(week.start, 6);
                    this.set('currentWeek', week);
                    break;
                case BirthdayPortlet.CONSTANTS.Monthly:
                	var month = this.get('currentMonth');
                	month.date = Y.Date.addMonths(month.date, -1);
                    this.set('currentMonth', month);
                    break;
                default:
                    this.set('currentDate', Y.Date.addDays(this.get('currentDate'), -1));
            }
        },
        
        /**
         *
         * Gets data from service
         *
         *
         */
        requestData: function(cmd, data, callback) {
            Y.io.request(this.get('resourceUrl') + cmd, {
               method: 'GET',
               data: data,
               dataType: 'json',
               on: {
                   success: function (e) {
                       var data = this.get('responseData');
                       callback(data);
                   }
               }
            });
        }
    }, {
        ATTRS: {
            viewMode: {
                value: 'day'
            },
            
            currentDate: {
                value: new Date()
            },
          
            currentMonth: {
                value: {
                	date: new Date(),
                	daysMap: {}
                }
            },
            
            currentMonthData: {
                value: {
                	dates: []
                }
            },
            
            currentWeek: {
                value: {
                    start: getStartWeekDate(new Date()),
                    end: getEndWeekDate(new Date())
                }
            },
            
            resourceUrl: {
                value: ''
            },
            
            pageSize: {
                value: 5
            },

            lastRequestStr: {
                value: null 
            } 
        }
    });
    
    BirthdayPortlet.CONSTANTS = {
        Daily: 'day',
        Weekly: 'week',
        Monthly: 'month',
        Days: 'days'
    };

    
    Y.BirthdayPortlet = BirthdayPortlet;

}, '1.0', {
    'requires': ['liferay-portlet-base', 'aui-io-request', 'yui-base', 'base-build', 'json-parse', 'json-stringify', 'datatype-date', 'datatype-date-format', 'handlebars']
});
