<%-- 
/**
 * Copyright (C) 2005-2014 Rivet Logic Corporation.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 */
--%>

<%@include file="/html/init.jsp" %>
<div class="portlet-birthdays">
  <!-- content -->
  <select name="view-mode" class="view-mode" autocomplete="off">
      <option value="day" selected="selected">Daily</option>
      <option value="week">Weekly</option>
      <option value="month">Monthly</option>
  </select>
  <ul class="pager">
      <li class="previous">
          <a href="#">&larr; Prev</a>
      </li>
      <li class="next">
          <a href="#">Next &rarr;</a>
      </li>
  </ul>
  <div class="bday-view-container">
      
  </div>
  <a class="btn btn-link btn-back" href="#"><i class="icon-chevron-left"></i> Back to today</a>

  <!-- TEMPLATES -->
  <script id="bday-big-date" type="text/x-handlebars-template">
      <div class="bdays-info-container">
          <div class="bdays-info clearfix">
              <div class="bdays-info-biggie"> {{day}}</div>
              <div class="bdays-info-label">
                  <div class="bdays-info-label-biggie">{{weekDay}}</div>
                  <div class="bdays-info-label-small">{{fullDate}}</div>
              </div>
          </div>
      </div>
  </script>
  <script id="bday-daily-list" type="text/x-handlebars-template">
      <ul class="nav nav-tabs nav-stacked">
          {{#each users}}
          <li class="active">
              <a href="{{#if profileUrl}}{{profileUrl}}{{else}}&num;{{/if}}" class="bday-person">
                  <img src="{{portrait}}" class="img-circle" alt="">
                  {{name}}
              </a>
          </li>
          {{/each}}
      </ul>
  </script>
  <script id="bday-grouped-list" type="text/x-handlebars-template">
      <ul class="nav nav-tabs nav-stacked accordionized">
          {{#each dates}}
          <li class="wrapper grouped-dates-wrapper">
              <a href="" class="toggle" data-dateStr="{{dateStr}}">
                  ({{usersNumber}}) {{formatISODate date}}  <i class="toggle-icon icon-plus"></i>
              </a>
              <ul class="content nav nav-tabs nav-stacked hidden">
                  {{#each users}}
                  <li class="active">
                      <a href="{{#if profileUrl}}{{profileUrl}}{{else}}&num;{{/if}}" class="bday-person">
                          <img src="{{portrait}}" class="img-circle" alt="">
                          {{name}}
                      </a>
                  </li>
                  {{/each}}
              </ul>
          </li>
          {{/each}}
      </ul>                            
  </script>
  <script id="bday-footer" type="text/x-handlebars-template">
      <div class="bottom-box text-center">
          {{#if empty}}
          <p>
              There are no birthdays to show this {{viewMode}}
              <br>
              <a data-upcoming="{{upcomingDate}}" href="javascript:void(0)" class="upcoming"><u>see upcoming date</u></a>
          </p>
          {{/if}}
      </div>
  </script>
  <script id="bday-week" type="text/x-handlebars-template">
      <div class="top-box text-center">
          <strong>{{start}} - {{end}}</strong>
      </div>
  </script>
  <script id="bday-month" type="text/x-handlebars-template">
      <div class="top-box text-center">
          <strong>{{month}}</strong>
      </div>
  </script>
  <script id="bday-show-more" class="bday-show-more" type="text/x-handlebars-template">
      <p>
          <a href="" class="show-more"><u>Show more</u></a>
      </p>
  </script>
</div>

<portlet:resourceURL var="getBirthdays">
</portlet:resourceURL>

<aui:script use="toutDisplay">
YUI().use('birthday-portlet', 'node', 'event', function(Y) {
    Y.on('domready', function() {
        new Y.BirthdayPortlet({
            namespace: '${pns}',
            resourceUrl: '${getBirthdays}'
        });
    });
});
</aui:script>
