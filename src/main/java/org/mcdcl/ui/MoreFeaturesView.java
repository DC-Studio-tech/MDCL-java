package org.mcdcl.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MoreFeaturesView extends VBox {
    private Label resultLabel;
    
    public MoreFeaturesView() {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        
        // 标题
        Label titleLabel = new Label("趣味功能");
        titleLabel.getStyleClass().add("section-title");
        
        // 创建功能区域
        GridPane featuresGrid = new GridPane();
        featuresGrid.setAlignment(Pos.TOP_CENTER);
        featuresGrid.setHgap(20);
        featuresGrid.setVgap(25);
        
        // 创建所有功能模块
        VBox jrpBox = createJRPFeature();
        VBox mcFortuneBox = createMCFortuneFeature();
        VBox mcQuoteBox = createMCQuoteFeature();
        VBox blockDivinationBox = createBlockDivinationFeature();
        VBox survivalChallengeBox = createSurvivalChallengeFeature();
        VBox mcQuizBox = createMCQuizFeature();
        VBox memeGeneratorBox = createMemeGeneratorFeature();
        VBox nicknameGeneratorBox = createNicknameGeneratorFeature();
        VBox achievementGeneratorBox = createAchievementGeneratorFeature();
        VBox deathMessageGeneratorBox = createDeathMessageGeneratorFeature();
        
        // 将功能模块添加到网格中
        featuresGrid.add(jrpBox, 0, 0);
        featuresGrid.add(mcFortuneBox, 1, 0);
        featuresGrid.add(mcQuoteBox, 2, 0);
        featuresGrid.add(blockDivinationBox, 0, 1);
        featuresGrid.add(survivalChallengeBox, 1, 1);
        featuresGrid.add(mcQuizBox, 2, 1);
        featuresGrid.add(memeGeneratorBox, 0, 2);
        featuresGrid.add(nicknameGeneratorBox, 1, 2);
        featuresGrid.add(achievementGeneratorBox, 2, 2);
        featuresGrid.add(deathMessageGeneratorBox, 0, 3);
        
        // 结果显示区域 - 使用滚动面板
        resultLabel = new Label("点击上方按钮，体验有趣功能");
        resultLabel.setWrapText(true);
        resultLabel.setMaxWidth(800);
        resultLabel.setPrefWidth(750);
        resultLabel.setStyle("-fx-background-color: rgba(240, 240, 240, 0.9); -fx-padding: 20px; " +
                "-fx-background-radius: 10px; -fx-font-size: 16px; -fx-text-fill: #000000; " +
                "-fx-font-weight: normal;");
        
        // 创建滚动面板来包含结果标签
        ScrollPane scrollPane = new ScrollPane(resultLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);  // 增加高度
        scrollPane.setMaxHeight(400);   // 设置最大高度
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // 隐藏水平滚动条
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // 需要时显示垂直滚动条
        
        // 创建一个带边框和标题的面板来包含滚动面板
        TitledPane resultPane = new TitledPane("结果显示", scrollPane);
        resultPane.setCollapsible(false); // 不可折叠
        resultPane.setPrefHeight(350);    // 增加高度
        resultPane.setMaxWidth(850);
        
        // 确保结果面板在界面底部有足够的空间
        VBox.setMargin(resultPane, new Insets(20, 0, 30, 0));
        
        getChildren().addAll(titleLabel, featuresGrid, resultPane);
        
        // 添加调试信息
        System.out.println("MoreFeaturesView 已初始化，结果标签已添加到界面");
    }
    
    // 修改所有结果输出方法，添加调试信息
    private void checkMiningFortune() {
        // 使用当前日期作为随机数种子，确保同一天生成相同的运势
        java.time.LocalDate today = java.time.LocalDate.now();
        int seed = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth() + 1; // +1 区分不同运势
        java.util.Random random = new java.util.Random(seed);
        
        String[] fortunes = {
            "今天挖矿运气爆棚，钻石在等你！",
            "小心岩浆，今天挖矿需谨慎。",
            "今天适合挖石头，会有意外发现。",
            "远离深渊，今天不适合深入矿洞。",
            "带上足够的火把，将有好运相伴。",
            "今天适合寻找远古残骸，下界等着你。",
            "矿洞中可能有宝藏，但也有危险。",
            "今天挖矿会有意外收获，可能找到附魔书。",
            "地下水特别多，带上水桶以防万一。",
            "今天适合挖掘绿宝石，村民交易会很划算。",
            "矿洞中的蜘蛛特别多，带上武器。",
            "今天适合寻找下界石英，会有大量收获。",
            "地下洞穴中有丰富的铁矿等着你。",
            "今天挖矿会遇到很多红石，适合收集。",
            "深层矿洞中可能有远古城市的入口。",
            "今天适合寻找青金石，附魔会很顺利。",
            "矿洞中的僵尸特别多，注意安全。",
            "今天适合挖掘煤炭，会有大量收获。",
            "地下可能有废弃矿井，里面有宝箱等着你。",
            "今天适合寻找金矿，会有意外惊喜。",
            "矿洞中可能有刷怪笼，可以考虑改造。",
            "今天适合挖掘铜矿，会发现大型矿脉。",
            "地下深处可能有巨型洞穴系统等待探索。",
            "今天适合寻找钻石，会找到品质极高的矿石。",
            "矿洞中的骷髅特别准，多带盾牌防御。",
            "今天适合挖掘黑曜石，会很顺利。",
            "地下水域中可能有溺尸群，小心应对。",
            "今天适合寻找深板岩中的矿物，收获颇丰。"
        };
        resultLabel.setText(fortunes[random.nextInt(fortunes.length)]);
    }
    
    private void checkPVPFortune() {
        // 使用当前日期作为随机数种子，确保同一天生成相同的运势
        java.time.LocalDate today = java.time.LocalDate.now();
        int seed = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth() + 2; // +2 区分不同运势
        java.util.Random random = new java.util.Random(seed);
        
        String[] fortunes = {
            "今天你将所向披靡！",
            "小心背后，今天要多加防范。",
            "适合团队作战，单独行动要谨慎。",
            "你的剑会为你带来胜利。",
            "今天不适合激烈战斗，不如去钓鱼。",
            "今天你的弓箭会特别准，远程攻击有优势。",
            "近战能力提升，适合冲锋陷阵。",
            "今天适合使用盾牌，防御能力增强。",
            "你的反应速度会特别快，有利于躲避攻击。",
            "今天适合埋伏战术，出其不意。",
            "你的暴击率会提高，造成更多伤害。",
            "今天适合使用药水，效果会更好。",
            "你的装备耐久度会特别高，不易损坏。",
            "今天适合水下战斗，会有优势。",
            "你的闪避能力增强，敌人难以命中你。",
            "今天适合高空作战，利用地形优势。",
            "你的连击能力提升，可以造成连续伤害。",
            "今天适合使用附魔武器，效果倍增。",
            "你的生命恢复速度加快，持久战有利。",
            "今天适合使用陷阱，出奇制胜。",
            "你的移动速度会特别快，有利于追击或逃跑。",
            "今天适合使用TNT等爆炸物，效果显著。",
            "你的装备附魔效果会特别好，战斗力提升。",
            "今天适合夜间作战，夜视能力增强。",
            "你的格挡成功率提高，减少受到的伤害。",
            "今天适合使用末影珍珠，机动性提高。",
            "你的饥饿值消耗减慢，持久战有优势。",
            "今天适合在下界作战，会有意想不到的优势。"
        };
        resultLabel.setText(fortunes[random.nextInt(fortunes.length)]);
    }
    
    private void checkBuildingFortune() {
        // 使用当前日期作为随机数种子，确保同一天生成相同的运势
        java.time.LocalDate today = java.time.LocalDate.now();
        int seed = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth() + 3; // +3 区分不同运势
        java.util.Random random = new java.util.Random(seed);
        
        String[] fortunes = {
            "灵感爆发，今天适合建造宏伟建筑！",
            "细节决定成败，今天适合装修。",
            "尝试新的建筑风格会有惊喜。",
            "今天建造的房子会特别稳固。",
            "不妨尝试红石建筑，会有新发现。",
            "今天适合建造中世纪风格的城堡。",
            "尝试使用新材料，会有意想不到的效果。",
            "今天适合建造水下建筑，会特别美观。",
            "尝试悬空建筑，会有独特的视觉效果。",
            "今天适合建造农场，产量会特别高。",
            "尝试使用玻璃和混凝土，现代风格脱颖而出。",
            "今天适合建造地下基地，隐蔽又实用。",
            "尝试使用不同颜色的混凝土粉末，色彩缤纷。",
            "今天适合建造红石机关，会特别灵敏。",
            "尝试使用阶梯和台阶，增加建筑层次感。",
            "今天适合建造空中花园，会特别美丽。",
            "尝试使用不同种类的木材，增加纹理变化。",
            "今天适合建造像素艺术，会特别精确。",
            "尝试使用珊瑚和海泡菜，增添异域风情。",
            "今天适合建造交通系统，会特别高效。",
            "尝试使用不同种类的石材，增加质感变化。",
            "今天适合建造村庄，村民会特别喜欢。",
            "尝试使用旗帜和盔甲架，增加装饰元素。",
            "今天适合建造主题公园，会特别有趣。",
            "尝试使用不同种类的栅栏，增加围墙变化。",
            "今天适合建造古代神庙，会特别庄严。",
            "尝试使用不同种类的门，增加入口变化。",
            "今天适合建造花园迷宫，会特别有挑战性。",
            "尝试使用不同种类的灯，增加照明效果。",
            "今天适合建造观景台，视野会特别开阔。"
        };
        resultLabel.setText(fortunes[random.nextInt(fortunes.length)]);
    }
    
    private void generateMCQuote() {
        // 使用当前日期作为随机数种子，确保同一天生成相同的名言
        java.time.LocalDate today = java.time.LocalDate.now();
        int seed = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth() + 4;
        java.util.Random random = new java.util.Random(seed);
        
        String[] quotes = {
            "钻石是最好的朋友！",
            "不要用木镐挖钻石。",
            "小心身后的苦力怕...",
            "挖地三尺，方见真金。",
            "不要在夜晚挑战末影龙。",
            "一失足成千古恨，再回头已百年身。",
            "不要和猪灵交易，除非你有黄金。",
            "末地是旅途的终点，也是新的开始。",
            "红石是工程师的梦想。",
            "不要挑战凋零，除非你已准备好。",
            "村民的交易有时比挖矿更划算。",
            "不要在下界搭建床，除非你想自杀。",
            "末影人害怕水，记住这一点。",
            "不要惹恼铁傀儡，除非你想飞上天。",
            "经验等级越高，附魔越好。",
            "不要在家门口放TNT，除非你不想要家了。",
            "信标是财富的象征。",
            "不要用剑攻击爬行者，除非你反应超快。",
            "下界合金是最坚固的材料。",
            "不要在熔岩上划船，除非你想游泳。",
            "末影箱是最安全的存储方式。",
            "不要在雨天使用三叉戟，除非你想被闪电击中。",
            "海绵是清理水的最佳工具。",
            "不要在沙漠中没有水，除非你想渴死。",
            "潜影贝的攻击会让你漂浮。",
            "不要在下界迷路，除非你带了指南针。",
            "附魔金苹果是最强大的食物。",
            "不要在暴风雪中迷路，除非你想冻死。",
            "龙蛋是最珍贵的装饰品。",
            "不要在洞穴中没有火把，除非你想被怪物包围。"
        };
        resultLabel.setText(quotes[random.nextInt(quotes.length)]);
    }
    
    private void performBlockDivination() {
        // 使用当前日期作为随机数种子，确保同一天生成相同的占卜结果
        java.time.LocalDate today = java.time.LocalDate.now();
        int seed = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth() + 5;
        java.util.Random random = new java.util.Random(seed);
        
        String[] blocks = {
            "钻石块", "泥土", "圆石", "黑曜石", "南瓜", "TNT", "蛋糕", "工作台",
            "末地石", "下界岩", "海绵", "金块", "铁块", "红石块", "青金石块",
            "煤炭块", "绿宝石块", "石英块", "干草块", "蜂蜜块", "海晶石",
            "紫珀块", "菌岩", "灵魂沙", "粘液块", "岩浆块", "冰块", "雪块",
            "书架", "箱子", "熔炉", "床", "唱片机", "附魔台", "信标"
        };
        
        String[] meanings = {
            "财运亨通！", "脚踏实地，稳扎稳打。", "平平淡淡才是真。", "坚韧不拔，无坚不摧。",
            "生活会很甜蜜。", "小心谨慎为妙。", "与朋友分享快乐。", "创意无限，前途光明。",
            "探索未知领域。", "勇往直前，不畏艰险。", "吸收新知识。", "价值连城，光芒四射。",
            "坚固可靠，值得信赖。", "能量充沛，思维活跃。", "智慧之源，学识渊博。",
            "蕴含能量，蓄势待发。", "珍贵独特，不可多得。", "纯洁无瑕，晶莹剔透。",
            "温暖舒适，安居乐业。", "甜蜜生活，美好时光。", "海洋之心，包容万物。",
            "神秘莫测，变幻莫测。", "生生不息，生命力旺盛。", "深沉内敛，厚积薄发。",
            "灵活多变，适应环境。", "热情如火，充满活力。", "冷静理智，头脑清醒。",
            "纯洁无瑕，心地善良。", "知识渊博，学富五车。", "储备丰富，未雨绸缪。",
            "转化能量，创造价值。", "休息充分，养精蓄锐。", "艺术气息，陶冶情操。",
            "智慧加持，能力提升。", "指引方向，照亮前路。"
        };
        
        int index = random.nextInt(blocks.length);
        resultLabel.setText("今日幸运方块：" + blocks[index] + "\n寓意：" + meanings[index]);
    }
    
    private void generateQuiz() {
        // 使用当前日期作为随机数种子，确保同一天生成相同的题目
        java.time.LocalDate today = java.time.LocalDate.now();
        int seed = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth() + 6;
        java.util.Random random = new java.util.Random(seed);
        
        String[] questions = {
            "问：如何制作工作台？\n答：将4个木板放在2x2的合成格子中。",
            "问：末影龙有多少生命值？\n答：200点（100颗心）。",
            "问：红石中继器的最大延迟是多少？\n答：4个红石刻。",
            "问：一个铁傀儡可以提供多少铁锭？\n答：3-5个铁锭。",
            "问：如何驯服狼？\n答：用骨头右键点击野生狼。",
            "问：下界传送门最少需要多少黑曜石？\n答：10个黑曜石。",
            "问：如何制作末影箱？\n答：在合成台上放置一个箱子，周围环绕8个末影之眼。",
            "问：村民的职业有多少种？\n答：13种职业。",
            "问：如何获得龙蛋？\n答：击败末影龙后，龙蛋会出现在传送门上方。",
            "问：如何制作信标？\n答：在合成台上放置一个下界之星，周围环绕3个黑曜石和5个玻璃。",
            "问：如何获得下界之星？\n答：击败凋灵后掉落。",
            "问：如何制作附魔金苹果？\n答：在合成台上放置一个苹果，周围环绕8个金块。",
            "问：如何获得鞘翅？\n答：在末地城中的末影船上可以找到。",
            "问：如何制作TNT？\n答：在合成台上放置5个火药和4个沙子。",
            "问：如何获得海绵？\n答：在海底神殿中或击败远古守卫者。",
            "问：如何制作唱片机？\n答：在合成台上放置8个木板和1个钻石。",
            "问：如何获得下界合金？\n答：将下界合金碎片与金锭在锻造台上合成。",
            "问：如何制作附魔台？\n答：在合成台上放置1本书，2个钻石和4个黑曜石。",
            "问：如何获得龙息？\n答：用空瓶右键点击末影龙的龙息。",
            "问：如何制作潜影盒？\n答：在合成台上放置一个箱子和一个潜影壳。",
            "问：如何获得潜影壳？\n答：击败潜影贝后掉落。",
            "问：如何制作漏斗？\n答：在合成台上放置5个铁锭和1个箱子。",
            "问：如何获得海晶石？\n答：在海底神殿中或击败守卫者。",
            "问：如何制作活塞？\n答：在合成台上放置3个木板，4个圆石，1个铁锭和1个红石粉。",
            "问：如何获得末影珍珠？\n答：击败末影人后掉落。",
            "问：如何制作末影之眼？\n答：在合成台上放置1个末影珍珠和1个烈焰粉。",
            "问：如何获得烈焰粉？\n答：击败烈焰人后掉落烈焰棒，将烈焰棒放入合成台可得到烈焰粉。",
            "问：如何制作书架？\n答：在合成台上放置6个木板和3本书。",
            "问：如何获得经验？\n答：击败怪物、挖矿、烧炼物品、钓鱼等。",
            "问：如何制作铁轨？\n答：在合成台上放置6个铁锭和1个木棍。"
        };
        resultLabel.setText(questions[random.nextInt(questions.length)]);
    }
    
    private void generateMeme() {
        // 使用当前日期作为随机数种子，确保同一天生成相同的表情包
        java.time.LocalDate today = java.time.LocalDate.now();
        int seed = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth() + 7;
        java.util.Random random = new java.util.Random(seed);
        
        String[] memes = {
            "当你用钻石镐挖石头时...",
            "当你第一次遇到苦力怕时...",
            "当你发现家里的箱子被偷时...",
            "当你意外掉进岩浆时...",
            "当你挖到钻石却没带铁镐时...",
            "当你在下界迷路时...",
            "当你的狗不小心掉进岩浆时...",
            "当你发现村民的交易特别坑时...",
            "当你第一次进入末地时...",
            "当你的房子被苦力怕炸了时...",
            "当你挖到第一块钻石时...",
            "当你发现自己挖了一小时却走回了原点时...",
            "当你的朋友偷走了你的附魔装备时...",
            "当你在生存模式中看到飞行的玩家时...",
            "当你发现自己的坐标是负数时...",
            "当你第一次遇到凋灵时...",
            "当你的床被别人偷走时...",
            "当你发现自己的农场被村民偷吃时...",
            "当你第一次使用鞘翅时...",
            "当你的猫把苦力怕吓跑时...",
            "当你挖矿时听到僵尸声音时...",
            "当你发现自己忘记带食物去探险时...",
            "当你的朋友在你的床旁边放TNT时...",
            "当你在雨天被闪电击中时...",
            "当你发现自己的钻石掉进岩浆时...",
            "当你第一次遇到远古守卫者时...",
            "当你的船在海上被海豚撞翻时...",
            "当你发现自己的红石机器不工作时...",
            "当你第一次使用末影珍珠时...",
            "当你的猪变成僵尸猪灵时..."
        };
        resultLabel.setText(memes[random.nextInt(memes.length)]);
    }
    
    private void generateNickname() {
        // 使用当前日期作为随机数种子，确保同一天生成相同的昵称
        java.time.LocalDate today = java.time.LocalDate.now();
        int seed = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth() + 8;
        java.util.Random random = new java.util.Random(seed);
        
        String[] prefixes = {
            "末影", "钻石", "铁匠", "红石", "苦力怕", "凋灵", "龙", "村民",
            "僵尸", "骷髅", "蜘蛛", "烈焰", "恶魂", "守卫者", "潜影", "凋零",
            "远古", "海洋", "沙漠", "丛林", "雪地", "山地", "洞穴", "下界",
            "末地", "黄金", "铁", "石", "木", "黑曜石", "青金石", "绿宝石"
        };
        
        String[] suffixes = {
            "战士", "大师", "猎手", "专家", "勇士", "王者", "领主", "统治者",
            "守护者", "探险家", "建筑师", "工程师", "魔法师", "炼金师", "农夫", "矿工",
            "猎人", "渔夫", "商人", "铁匠", "学者", "法师", "骑士", "刺客",
            "游侠", "术士", "牧师", "战斗机", "狂战士", "守卫", "斗士", "英雄"
        };
        
        String nickname = prefixes[random.nextInt(prefixes.length)] + 
                        suffixes[random.nextInt(suffixes.length)];
        resultLabel.setText("你的随机昵称：" + nickname);
    }
    
    private void generateAchievement() {
        // 使用当前日期作为随机数种子，确保同一天生成相同的成就
        java.time.LocalDate today = java.time.LocalDate.now();
        int seed = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth() + 9;
        java.util.Random random = new java.util.Random(seed);
        
        String[] achievements = {
            "【超级矿工】\n一天内挖到100个钻石",
            "【建筑大师】\n建造一座高度超过200格的建筑",
            "【驯龙高手】\n驯服5只末影龙",
            "【红石专家】\n制作一台全自动农场",
            "【末地征服者】\n击败末影龙并获得龙蛋",
            "【下界探险家】\n探索所有下界生物群系",
            "【村庄守护者】\n击退一次村庄袭击",
            "【海洋霸主】\n击败远古守卫者",
            "【末地掠夺者】\n找到并掠夺末地城",
            "【凋灵猎人】\n击败凋灵并获得下界之星",
            "【生存大师】\n在极限模式下生存100天",
            "【农业专家】\n种植所有作物并收获",
            "【动物朋友】\n驯服所有可驯服的动物",
            "【附魔大师】\n获得所有最高等级的附魔",
            "【收藏家】\n收集所有唱片",
            "【探险家】\n探索所有生物群系",
            "【钓鱼达人】\n钓上一条附魔钓鱼竿",
            "【交易专家】\n与所有职业的村民交易",
            "【药水大师】\n酿造所有种类的药水",
            "【飞行专家】\n使用鞘翅飞行1000格",
            "【地下探险家】\n探索一个完整的矿井",
            "【海底寻宝者】\n找到一艘沉船并获得宝藏",
            "【沙漠冒险家】\n找到一座沙漠神殿并获得宝藏",
            "【丛林探险家】\n找到一座丛林神庙并获得宝藏",
            "【雪地探险家】\n找到一座雪屋并获得宝藏",
            "【下界堡垒征服者】\n找到并征服一座下界堡垒",
            "【远古城市探险家】\n找到并探索一座远古城市",
            "【深海探险家】\n探索海底神殿并获得海绵",
            "【末地探险家】\n探索末地并找到末地城",
            "【生物收集家】\n收集所有生物的刷怪蛋"
        };
        resultLabel.setText(achievements[random.nextInt(achievements.length)]);
    }
    
    private void generateDeathMessage() {
        // 使用当前日期作为随机数种子，确保同一天生成相同的死亡信息
        java.time.LocalDate today = java.time.LocalDate.now();
        int seed = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth() + 10;
        java.util.Random random = new java.util.Random(seed);
        
        String[] messages = {
            "玩家被苦力怕吓得心脏骤停",
            "玩家试图在岩浆中游泳",
            "玩家忘记了降落伞",
            "玩家被自己放置的TNT炸飞",
            "玩家被末影人传送到虚空",
            "玩家被僵尸围攻致死",
            "玩家被骷髅射成了刺猬",
            "玩家被蜘蛛吓得跳崖",
            "玩家被凋灵的头颅击中",
            "玩家被恶魂的火球烧成灰烬",
            "玩家被守卫者的激光射穿",
            "玩家被潜影贝的子弹击中",
            "玩家被远古守卫者的激光蒸发",
            "玩家被末影龙的龙息融化",
            "玩家被凋零效果消耗",
            "玩家被凋零效果消耗",
            "玩家在睡觉时被梦魇吞噬",
            "玩家被猪灵的金剑刺穿",
            "玩家在尝试与末影人对视时死亡",
            "玩家被自己的箭反弹致命",
            "玩家在挖矿时触发了陷阱",
            "玩家被突如其来的沙子埋葬",
            "玩家在尝试驯服北极熊时被拍死",
            "玩家被自己的狼误伤致死",
            "玩家在尝试与村民交易时被铁傀儡击飞",
            "玩家在探索废弃矿井时踩空",
            "玩家被自己的红石装置电死",
            "玩家在尝试收集蜂蜜时被蜜蜂蛰死",
            "玩家在尝试与海豚共泳时溺水",
            "玩家被自己的剑反伤致命",
            "玩家在尝试与猪灵交易时被围攻",
            "玩家在尝试与掠夺者首领交手时被秒杀",
            "玩家在尝试骑马跳过峡谷时失败",
            "玩家被自己的宠物鹦鹉啄死",
            "玩家在尝试与僵尸村民交易时被感染",
            "玩家在尝试与末影龙合影时被尾巴扫飞",
            "玩家被自己的附魔书诅咒",
            "玩家在尝试与凋灵合影时被秒杀",
            "玩家在尝试与远古守卫者对视时被石化",
            "玩家被自己的信标光柱烧焦"
        };
        resultLabel.setText(messages[random.nextInt(messages.length)]);
    }
    
    private void generateSurvivalChallenge() {
        // 使用当前日期作为随机数种子，确保同一天生成相同的挑战
        java.time.LocalDate today = java.time.LocalDate.now();
        int seed = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth() + 11;
        java.util.Random random = new java.util.Random(seed);
        
        String[] challenges = {
            "【无木挑战】\n不允许破坏任何木头，只能从村庄或废弃矿井中获取木制品。",
            "【地下生存】\n整个游戏只能在地表以下生活，不能见到天空。",
            "【和平主义者】\n不能主动攻击任何生物，只能使用陷阱或让生物互相攻击。",
            "【素食主义者】\n只能吃植物性食物，不能吃任何肉类。",
            "【岛屿生存】\n只能在一个小岛上生存，不能离开这个岛。",
            "【无铁挑战】\n不能使用任何铁制工具或武器，只能用石头或钻石。",
            "【单一生物群系】\n只能在一种生物群系中生存，不能进入其他生物群系。",
            "【无床挑战】\n不能使用床，必须面对每一个夜晚。",
            "【无储存挑战】\n不能使用任何箱子或容器存储物品。",
            "【极简主义者】\n背包中最多只能携带9种不同的物品。",
            "【游牧生活】\n每7天必须搬家一次，不能在同一地点停留太久。",
            "【无附魔挑战】\n不能使用任何附魔物品，必须靠原始装备生存。",
            "【无弓挑战】\n不能使用弓箭，只能近战。",
            "【无盔甲挑战】\n不能穿戴任何盔甲，必须依靠躲避来保护自己。",
            "【水下生存】\n基地必须建在水下，只能在水中或水下活动。",
            "【熔岩行者】\n基地必须建在下界的熔岩海上，主要在熔岩附近活动。",
            "【单一工具】\n只能使用一种工具（如只用斧头）完成所有任务。",
            "【无采矿挑战】\n不能挖掘任何矿石，只能从村庄、废弃矿井或交易中获取金属。",
            "【极限高度】\n基地必须建在Y坐标200以上，主要在高空活动。",
            "【极限深度】\n基地必须建在Y坐标0以下，主要在深层活动。",
            "【无交易挑战】\n不能与村民交易，必须自己获取所有资源。",
            "【无钓鱼挑战】\n不能钓鱼，必须通过其他方式获取食物。",
            "【无农业挑战】\n不能种植作物，只能收集野生食物或狩猎。",
            "【无红石挑战】\n不能使用任何红石装置，一切都要手动操作。",
            "【极限生存】\n只有一条命，死亡后必须删除世界。",
            "【无跳跃挑战】\n不能跳跃，必须通过建造楼梯或使用水电梯上升。",
            "【无破坏挑战】\n不能破坏任何自然生成的方块，只能在已有空间中建造。",
            "【无合成挑战】\n不能使用工作台，只能使用已有物品或通过交易获取。",
            "【黑暗生存】\n不能使用任何光源，必须在黑暗中生存。",
            "【极速挑战】\n必须在30个游戏日内击败末影龙。"
        };
        resultLabel.setText(challenges[random.nextInt(challenges.length)]);
    }
    
    private VBox createJRPFeature() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #FFE4E1; -fx-padding: 10; -fx-background-radius: 5;");
        
        Label titleLabel = new Label("今日人品");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        Button checkButton = new Button("查看今日人品");
        checkButton.setOnAction(e -> checkJRP());
        
        box.getChildren().addAll(titleLabel, checkButton);
        return box;
    }
    
    private void checkJRP() {
        // 使用当前日期作为随机数种子，确保同一天生成相同的人品值
        java.time.LocalDate today = java.time.LocalDate.now();
        int seed = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth();
        java.util.Random random = new java.util.Random(seed);
        
        int score = random.nextInt(100) + 1; // 生成1-100的随机数
        
        String result = String.format("您今日的人品值为：%d\n", score);
        if(score >= 90) {
            result += "今天运气爆棚！";
        } else if(score >= 70) {
            result += "今天运气不错！";
        } else if(score >= 50) {
            result += "今天运气一般。";
        } else if(score >= 30) {
            result += "今天运气较差...";
        } else {
            result += "今天最好待在家里...";
        }
        resultLabel.setText(result);
        System.out.println("设置结果文本: " + result); // 添加调试输出
    }
    
    private VBox createMCQuizFeature() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #E1F5FE; -fx-padding: 10; -fx-background-radius: 5;");
        
        Label titleLabel = new Label("MC知识问答");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        Button quizButton = new Button("随机问题");
        quizButton.setOnAction(e -> generateQuiz());
        
        box.getChildren().addAll(titleLabel, quizButton);
        return box;
    }
    
    private VBox createMemeGeneratorFeature() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #F3E5F5; -fx-padding: 10; -fx-background-radius: 5;");
        
        Label titleLabel = new Label("MC表情包生成器");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        Button generateButton = new Button("生成表情包");
        generateButton.setOnAction(e -> generateMeme());
        
        box.getChildren().addAll(titleLabel, generateButton);
        return box;
    }
    
    private VBox createNicknameGeneratorFeature() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #FFF9C4; -fx-padding: 10; -fx-background-radius: 5;");
        
        Label titleLabel = new Label("MC昵称生成器");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        Button generateButton = new Button("生成昵称");
        generateButton.setOnAction(e -> generateNickname());
        
        box.getChildren().addAll(titleLabel, generateButton);
        return box;
    }
    
    private VBox createAchievementGeneratorFeature() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #DCEDC8; -fx-padding: 10; -fx-background-radius: 5;");
        
        Label titleLabel = new Label("MC成就生成器");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        Button generateButton = new Button("生成成就");
        generateButton.setOnAction(e -> generateAchievement());
        
        box.getChildren().addAll(titleLabel, generateButton);
        return box;
    }
    
    private VBox createDeathMessageGeneratorFeature() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #FFCCBC; -fx-padding: 10; -fx-background-radius: 5;");
        
        Label titleLabel = new Label("MC死亡信息生成器");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        Button generateButton = new Button("生成死亡信息");
        generateButton.setOnAction(e -> generateDeathMessage());
        
        box.getChildren().addAll(titleLabel, generateButton);
        return box;
    }
    
    private VBox createSurvivalChallengeFeature() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #FFECB3; -fx-padding: 10; -fx-background-radius: 5;");
        
        Label titleLabel = new Label("生存挑战生成器");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        Button generateButton = new Button("生成挑战");
        generateButton.setOnAction(e -> generateSurvivalChallenge());
        
        box.getChildren().addAll(titleLabel, generateButton);
        return box;
    }
    
    private VBox createMCFortuneFeature() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #E0FFFF; -fx-padding: 10; -fx-background-radius: 5;");
        
        Label titleLabel = new Label("Minecraft运势");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button miningButton = new Button("挖矿运势");
        Button pvpButton = new Button("PVP运势");
        Button buildButton = new Button("建筑运势");
        
        miningButton.setOnAction(e -> checkMiningFortune());
        pvpButton.setOnAction(e -> checkPVPFortune());
        buildButton.setOnAction(e -> checkBuildingFortune());
        
        buttonBox.getChildren().addAll(miningButton, pvpButton, buildButton);
        box.getChildren().addAll(titleLabel, buttonBox);
        return box;
    }
    
    private VBox createMCQuoteFeature() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #E8F5E9; -fx-padding: 10; -fx-background-radius: 5;");
        
        Label titleLabel = new Label("MC名言生成器");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        Button generateButton = new Button("生成名言");
        generateButton.setOnAction(e -> generateMCQuote());
        
        box.getChildren().addAll(titleLabel, generateButton);
        return box;
    }
    
    private VBox createBlockDivinationFeature() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #FFE0B2; -fx-padding: 10; -fx-background-radius: 5;");
        
        Label titleLabel = new Label("方块占卜");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        Button divinateButton = new Button("开始占卜");
        divinateButton.setOnAction(e -> performBlockDivination());
        
        box.getChildren().addAll(titleLabel, divinateButton);
        return box;
    }
}