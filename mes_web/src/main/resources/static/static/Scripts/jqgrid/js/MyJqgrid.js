/**
 * Created by vfun02 on 2017/9/28.
 */
$.jgrid.extend({
    setGroupHeaders: function (o) {
        o = $.extend({
            useColSpanStyle: false,
            groupHeaders: []
        }, o || {});
        return this.each(function () {
            var ts = this,
                i, cmi, skip = 0, $tr, $colHeader, th, $th, thStyle,
                iCol,
                cghi,
                //startColumnName,
                numberOfColumns,
                titleText,
                cVisibleColumns,
                className,
                colModel = ts.p.colModel,
                cml = colModel.length,
                ths = ts.grid.headers,
                $htable = $("table.ui-jqgrid-htable", ts.grid.hDiv),
                $trLabels = $htable.children("thead").children("tr.ui-jqgrid-labels:last").addClass("jqg-second-row-header"),
                $thead = $htable.children("thead"),
                $theadInTable,
                $firstHeaderRow = $htable.find(".jqg-first-row-header"),
                //classes = $.jgrid.styleUI[($t.p.styleUI || 'jQueryUI')]['grouping'],
                base = $.jgrid.styleUI[(ts.p.styleUI || 'jQueryUI')].base;
            if (!ts.p.groupHeader) {
                ts.p.groupHeader = [];
            }
            ts.p.groupHeader.push(o);
            if ($firstHeaderRow[0] === undefined) {
                $firstHeaderRow = $('<tr>', {
                    role: "row",
                    "aria-hidden": "true"
                }).addClass("jqg-first-row-header").css("height", "auto");
            } else {
                $firstHeaderRow.empty();
            }
            var $firstRow,
                inColumnHeader = function (text, columnHeaders) {
                    var length = columnHeaders.length, i;
                    for (i = 0; i < length; i++) {
                        if (columnHeaders[i].startColumnName === text) {
                            return i;
                        }
                    }
                    return -1;
                };

            $(ts).prepend($thead);
            $tr = $('<tr>', {role: "row"}).addClass("ui-jqgrid-labels jqg-third-row-header");
            for (i = 0; i < cml; i++) {
                th = ths[i].el;
                $th = $(th);
                cmi = colModel[i];
                // build the next cell for the first header row
                thStyle = {height: '0px', width: ths[i].width + 'px', display: (cmi.hidden ? 'none' : '')};
                $("<th>", {role: 'gridcell'}).css(thStyle).addClass("ui-first-th-" + ts.p.direction).appendTo($firstHeaderRow);

                th.style.width = ""; // remove unneeded style
                iCol = inColumnHeader(cmi.name, o.groupHeaders);
                if (iCol >= 0) {
                    cghi = o.groupHeaders[iCol];
                    numberOfColumns = cghi.numberOfColumns;
                    titleText = cghi.titleText;
                    className = cghi.className || "";
                    // caclulate the number of visible columns from the next numberOfColumns columns
                    for (cVisibleColumns = 0, iCol = 0; iCol < numberOfColumns && (i + iCol < cml); iCol++) {
                        if (!colModel[i + iCol].hidden) {
                            cVisibleColumns++;
                        }
                    }

                    // The next numberOfColumns headers will be moved in the next row
                    // in the current row will be placed the new column header with the titleText.
                    // The text will be over the cVisibleColumns columns
                    $colHeader = $('<th>').attr({role: "columnheader"})
                        .addClass(base.headerBox + " ui-th-column-header ui-th-" + ts.p.direction + " " + className)
                        //.css({'height':'22px', 'border-top': '0 none'})
                        .html(titleText);
                    if (cVisibleColumns > 0) {
                        $colHeader.attr("colspan", String(cVisibleColumns));
                    }
                    if (ts.p.headertitles) {
                        $colHeader.attr("title", $colHeader.text());
                    }
                    // hide if not a visible cols
                    if (cVisibleColumns === 0) {
                        $colHeader.hide();
                    }

                    $th.before($colHeader); // insert new column header before the current
                    $tr.append(th);         // move the current header in the next row

                    // set the coumter of headers which will be moved in the next row
                    skip = numberOfColumns - 1;
                } else {
                    if (skip === 0) {
                        if (o.useColSpanStyle) {
                            // expand the header height to two rows
                            // $th.attr("rowspan", "2");
                            var rowspanlen = $th.parent().siblings('tr').length;
                            if (rowspanlen == 0) {
                                rowspanlen = 2;
                            } else {
                                rowspanlen++;
                            }
                            $th.attr("rowspan", rowspanlen);
                        } else {
                            $('<th>', {role: "columnheader"})
                                .addClass(base.headerBox + " ui-th-column-header ui-th-" + ts.p.direction)
                                .css({"display": cmi.hidden ? 'none' : ''})
                                .insertBefore($th);
                            $tr.append(th);
                        }
                    } else {
                        // move the header to the next row
                        //$th.css({"padding-top": "2px", height: "19px"});
                        $tr.append(th);
                        skip--;
                    }
                }
            }
            $theadInTable = $(ts).children("thead");
            $theadInTable.prepend($firstHeaderRow);
            $tr.insertAfter($trLabels);
            $htable.append($theadInTable);

            if (o.useColSpanStyle) {
                // Increase the height of resizing span of visible headers
                $htable.find("span.ui-jqgrid-resize").each(function () {
                    var $parent = $(this).parent();
                    if ($parent.is(":visible")) {
                        this.style.cssText = 'height: ' + $parent.height() + 'px !important; cursor: col-resize;';
                    }
                });

                // Set position of the sortable div (the main lable)
                // with the column header text to the middle of the cell.
                // One should not do this for hidden headers.
                $htable.find("div.ui-jqgrid-sortable").each(function () {
                    var $ts = $(this), $parent = $ts.parent();
                    if ($parent.is(":visible") && $parent.is(":has(span.ui-jqgrid-resize)")) {
                        // minus 4px from the margins of the resize markers
                        $ts.css('top', ($parent.height() - $ts.outerHeight()) / 2 - 4 + 'px');
                    }
                });
            }

            $firstRow = $theadInTable.find("tr.jqg-first-row-header");
            $(ts).on('jqGridResizeStop.setGroupHeaders', function (e, nw, idx) {
                $firstRow.find('th').eq(idx)[0].style.width = nw + "px";
            });
        });
    }
});

//module begin
$.extend($.jgrid, {
    checkValues: function (val, valref, customobject, nam) {
        var edtrul, i, nm, dft, len, g = this, cm = g.p.colModel,
            msg = $.jgrid.getRegional(this, 'edit.msg'), fmtdate;
        if (customobject === undefined) {
            if (typeof valref === 'string') {
                for (i = 0, len = cm.length; i < len; i++) {
                    if (cm[i].name === valref) {
                        edtrul = cm[i].editrules;
                        valref = i;
                        if (cm[i].formoptions != null) {
                            nm = cm[i].formoptions.label;
                        }
                        break;
                    }
                }
            } else if (valref >= 0) {
                edtrul = cm[valref].editrules;
            }
        } else {
            edtrul = customobject;
            nm = nam === undefined ? "_" : nam;
        }
        if (edtrul) {
            if (!nm) {
                nm = g.p.colNames != null ? g.p.colNames[valref] : cm[valref].label;
            }
            if (edtrul.required === true) {
                if ($.jgrid.isEmpty(val)) {
                    return [false, nm + ": " + msg.required, ""];
                }
            }
            // force required
            var rqfield = edtrul.required === false ? false : true;
            if (edtrul.number === true) {
                if (!(rqfield === false && $.jgrid.isEmpty(val))) {
                    if (isNaN(val) || val.trim() != val) {
                        return [false, nm + ": " + msg.number, ""];
                    }
                }
            }
            if (edtrul.minValue !== undefined && !isNaN(edtrul.minValue)) {
                if (parseFloat(val) < parseFloat(edtrul.minValue)) {
                    return [false, nm + ": " + msg.minValue + " " + edtrul.minValue, ""];
                }
            }
            if (edtrul.maxValue !== undefined && !isNaN(edtrul.maxValue)) {
                if (parseFloat(val) > parseFloat(edtrul.maxValue)) {
                    return [false, nm + ": " + msg.maxValue + " " + edtrul.maxValue, ""];
                }
            }
            var filter;
            if (edtrul.email === true) {
                if (!(rqfield === false && $.jgrid.isEmpty(val))) {
                    // taken from $ Validate plugin
                    filter = /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i;
                    if (!filter.test(val)) {
                        return [false, nm + ": " + msg.email, ""];
                    }
                }
            }
            if (edtrul.integer === true) {
                if (!(rqfield === false && $.jgrid.isEmpty(val))) {
                    if (isNaN(val)) {
                        return [false, nm + ": " + msg.integer, ""];
                    }
                    if ((val % 1 !== 0) || (val.indexOf('.') !== -1)) {
                        return [false, nm + ": " + msg.integer, ""];
                    }
                }
            }
            if (edtrul.date === true) {
                if (!(rqfield === false && $.jgrid.isEmpty(val))) {
                    if (cm[valref].formatoptions && cm[valref].formatoptions.newformat) {
                        dft = cm[valref].formatoptions.newformat;
                        fmtdate = $.jgrid.getRegional(g, 'formatter.date.masks');
                        if (fmtdate && fmtdate.hasOwnProperty(dft)) {
                            dft = fmtdate[dft];
                        }
                    } else {
                        dft = cm[valref].datefmt || "Y-m-d";
                    }
                    if (!$.jgrid.checkDate(dft, val)) {
                        return [false, nm + ": " + msg.date + " - " + dft, ""];
                    }
                }
            }
            if (edtrul.time === true) {
                if (!(rqfield === false && $.jgrid.isEmpty(val))) {
                    if (!$.jgrid.checkTime(val)) {
                        return [false, nm + ": " + msg.date + " - hh:mm (am/pm)", ""];
                    }
                }
            }
            if (edtrul.url === true) {
                if (!(rqfield === false && $.jgrid.isEmpty(val))) {
                    filter = /^(((https?)|(ftp)):\/\/([\-\w]+\.)+\w{2,3}(\/[%\-\w]+(\.\w{2,})?)*(([\w\-\.\?\\\/+@&#;`~=%!]*)(\.\w{2,})?)*\/?)/i;
                    if (!filter.test(val)) {
                        return [false, nm + ": " + msg.url, ""];
                    }
                }
            }
            if (edtrul.custom === true) {
                if (!(rqfield === false && $.jgrid.isEmpty(val))) {
                    if ($.isFunction(edtrul.custom_func)) {
                        var ret = edtrul.custom_func.call(g, val, nm, valref);
                        return $.isArray(ret) ? ret : [false, msg.customarray, ""];
                    }
                    return [false, msg.customfcheck, ""];
                }
            }
        }
        return [true, "", ""];
    }
});
