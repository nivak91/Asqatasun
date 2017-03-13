$(document).ready(function() {
    buildMetricsDonut();
});

buildMetricsDonut = function() {
    /*------------------------------------------------------------------------
    ----------------------Score donut-----------------------------------------
    --------------------------------------------------------------------------*/
    var scoreSelector = "WAQMResult",
        EnhancedWAQMselector = "EnhancedWAQMResult",
        WAQMscoreElement = d3.select('#'+scoreSelector),
        EnhancedWAQMscoreElement = d3.select('#'+EnhancedWAQMselector),
        WAQMmetric,
        WAQMpercentile,
        EnhancedWAQMmetric,
        EnhancedWAQMpercentile,
        score;
    if (WAQMscoreElement != null) {
        WAQMmetric = WAQMscoreElement.select('div:first-child').text();
        if (WAQMmetric != 'Echec' || WAQMmetric != 'Fail' ) {
            score = parseInt(WAQMmetric, 10);
            drawWAQMscore(
                        WAQMscoreElement,
                        score,
                        160, // width
                        160,  // height
                        2.1, //divider
                        17,  // radius
                        "d3-"+scoreSelector, //Id of div parent
                        null,
                        null,
                        true, // addText
                        -44, // asqatasunMeterXOffset
                        -28, // asqatasunMeterXOffset
                        -1, // scoreXOffset
                        5, // scoreYOffset
                        36, // percentXOffset
                        15, // percentYOffset
                        -5, // maxScoreXOffset
                        45, // maxScorePercentXOffset
                        22);
                        WAQMscoreElement.select('div:first-child').remove();



        }

        WAQMpercentile = WAQMscoreElement.select('div:first-child').text();
        if (WAQMpercentile != 'Echec' || WAQMpercentile != 'Fail' ) {
            score = parseInt(WAQMpercentile, 10);
            drawWAQMpercentile(
                                WAQMscoreElement,
                                score,
                                160, // width
                                160,  // height
                                2.1, //divider
                                17,  // radius
                                "d3-"+scoreSelector, //Id of div parent
                                null,
                                null,
                                true, // addText
                                -44, // asqatasunMeterXOffset
                                -28, // asqatasunMeterXOffset
                                -1, // scoreXOffset
                                5, // scoreYOffset
                                36, // percentXOffset
                                15, // percentYOffset
                                -5, // maxScoreXOffset
                                45, // maxScorePercentXOffset
                                22);

                    WAQMscoreElement.select('div:first-child').remove();

                }
    }
    if (EnhancedWAQMscoreElement != null) {
            EnhancedWAQMmetric = EnhancedWAQMscoreElement.select('div:first-child').text();
            if (EnhancedWAQMmetric != 'Echec' || EnhancedWAQMmetric != 'Fail' ) {
                score = parseInt(EnhancedWAQMmetric, 10);
                drawEnhancedWAQMscore(
                            EnhancedWAQMscoreElement,
                            score,
                            160, // width
                            160,  // height
                            2.1, //divider
                            17,  // radius
                            "d3-"+EnhancedWAQMselector, //Id of div parent
                            null,
                            null,
                            true, // addText
                            -44, // asqatasunMeterXOffset
                            -28, // asqatasunMeterXOffset
                            -1, // scoreXOffset
                            5, // scoreYOffset
                            36, // percentXOffset
                            15, // percentYOffset
                            -5, // maxScoreXOffset
                            45, // maxScorePercentXOffset
                            22);
                        EnhancedWAQMscoreElement.select('div:first-child').remove();


            }

            EnhancedWAQMpercentile = EnhancedWAQMscoreElement.select('div:first-child').text();
            if (EnhancedWAQMpercentile != 'Echec' || EnhancedWAQMpercentile != 'Fail' ) {
                score = parseInt(EnhancedWAQMpercentile, 10);
                drawEnhancedWAQMpercentile(
                                    EnhancedWAQMscoreElement,
                                    score,
                                    160, // width
                                    160,  // height
                                    2.1, //divider
                                    17,  // radius
                                    "d3-"+EnhancedWAQMselector, //Id of div parent
                                    null,
                                    null,
                                    true, // addText
                                    -44, // asqatasunMeterXOffset
                                    -28, // asqatasunMeterXOffset
                                    -1, // scoreXOffset
                                    5, // scoreYOffset
                                    36, // percentXOffset
                                    15, // percentYOffset
                                    -5, // maxScoreXOffset
                                    45, // maxScorePercentXOffset
                                    22);
                       EnhancedWAQMscoreElement.select('div:first-child').remove();

                    }
        }
};