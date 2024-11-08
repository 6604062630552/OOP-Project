import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.*;

public class MatchCards {

    class Card {
        String cardName;
        ImageIcon cardImageIcon;

        Card(String cardName, ImageIcon cardImageIcon) {
            this.cardName = cardName;
            this.cardImageIcon = cardImageIcon;
        }

        public String toString() {
            return cardName;
        }
    }

    String[] cardList = {"ace", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
    int rows = 4;
    int columns = 5;
    int cardWidth = 90;
    int cardHeight = 128;

    ArrayList<Card> cardSet;
    ImageIcon cardBackImageIcon;

    int boardWidth = columns * cardWidth + 200; //650
    int boardHeight = rows * cardHeight + 200; //712

    JFrame frame;
    JPanel timerPanel = new JPanel();
    JLabel timeLabel = new JLabel();
    JLabel winLabel = new JLabel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();

    ArrayList<JButton> board;
    Timer hideCardTimer;
    Timer gameTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;
    long startTime;
    long elapsedTime;
    int matchedPairs = 0;

    MatchCards() {
        frame = new JFrame("Matching Card Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        // เริ่มต้นที่หน้าแรก
        showWelcomeScreen();
        frame.setVisible(true);
    }

    private void showWelcomeScreen() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
    
        // ตั้งค่าให้ขนาดหน้าต่างเท่ากับขนาดของหน้าเล่นเกม
        frame.setSize(boardWidth, boardHeight);
        frame.setPreferredSize(new Dimension(boardWidth, boardHeight));
    
        // ตั้งค่าพื้นหลังเป็นรูปภาพ
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/img/bg.png"));
        JLabel backgroundLabel = new JLabel(backgroundIcon);
    
        // สร้าง JPanel สำหรับจัดกลุ่มปุ่ม Play และ Exit ให้อยู่ตรงกลาง
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // ใช้ BoxLayout เพื่อให้ปุ่มอยู่ในแนวตั้ง
        centerPanel.setOpaque(false); // ตั้งค่าให้โปร่งใสเพื่อให้พื้นหลังสามารถแสดงผลได้
    
        // เพิ่มช่องว่างให้กับ panel (ลดค่าลงเพื่อให้ปุ่มต่ำลง)
        centerPanel.add(Box.createVerticalStrut(250));  // เพิ่มช่องว่างจากด้านบนของหน้าจอ (ปรับค่าเป็น 250)
    
        // ชื่อเกม
        JLabel titleLabel = new JLabel("Matching Card Game", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // ตั้งให้ชื่อเกมอยู่ตรงกลางในแกน X
        centerPanel.add(titleLabel);
    
        // เพิ่มช่องว่างระหว่างชื่อเกมและปุ่ม
        centerPanel.add(Box.createVerticalStrut(50));
    
        // ปุ่ม Play
        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.PLAIN, 20));
        playButton.setBackground(new Color(255, 255, 255));
        playButton.setForeground(Color.BLACK);
        playButton.setBorderPainted(false);
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setFocusPainted(false); // ปิดเส้นขอบเมื่อเลือกปุ่ม
        playButton.setPreferredSize(new Dimension(150, 50)); // กำหนดขนาดของปุ่ม

        playButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                playButton.setBackground(new Color(200,200,200));
            }
        
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                playButton.setBackground(new Color(255,255,255));
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGameScreen(); // ลบองค์ประกอบบนหน้าแรกออก แล้วแสดงหน้าเกม
            }
        });
        centerPanel.add(playButton);
    
        // เพิ่มช่องว่างระหว่างปุ่ม Play และ Exit
        centerPanel.add(Box.createVerticalStrut(20));
    
        // ปุ่ม Exit
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        exitButton.setBackground(new Color(255, 0, 0));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorderPainted(false);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setFocusPainted(false); // ปิดเส้นขอบเมื่อเลือกปุ่ม
        exitButton.setPreferredSize(new Dimension(150, 50)); // กำหนดขนาดของปุ่ม

        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitButton.setBackground(new Color(200,0,0));
            }
        
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitButton.setBackground(new Color(255,0,0));
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        centerPanel.add(exitButton);
    
        // เพิ่ม panel ที่มีปุ่ม Play และ Exit ไปยังหน้าจอ
        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.add(centerPanel, BorderLayout.CENTER); // ตั้งให้ panel อยู่ตรงกลาง
    
        frame.add(backgroundLabel, BorderLayout.CENTER);
    
        frame.revalidate();
        frame.repaint();
    }
    
    
    

    private void showGameScreen() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(0, 100, 0));

        setupCards();
        shuffleCards();

        timerPanel.setLayout(new BorderLayout(20, 5));
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setText("Time: 00:00:00");
        timerPanel.add(timeLabel, BorderLayout.WEST);
        timerPanel.add(winLabel, BorderLayout.CENTER);
        timerPanel.setBackground(new Color(0, 100, 0));
        frame.add(timerPanel, BorderLayout.NORTH);

        board = new ArrayList<>();
        boardPanel.setPreferredSize(new Dimension(boardWidth, boardHeight));
        boardPanel.setLayout(new GridLayout(rows, columns, 0, 0));

        boardPanel.setBackground(new Color(0, 100, 0));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth,cardHeight));
            tile.setMinimumSize(new Dimension(cardWidth,cardHeight));
            tile.setMaximumSize(new Dimension(cardWidth,cardHeight));
            tile.setOpaque(false);
            tile.setContentAreaFilled(false);
            tile.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
            tile.setFocusPainted(false);
            tile.setIcon(cardBackImageIcon);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                handleTileClick(e);
              }  
            });
            board.add(tile);
            boardPanel.add(tile);
        }
        frame.add(boardPanel, BorderLayout.CENTER);

        restartGamePanel.setBackground(new Color(0, 100, 0));
        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.setText("Restart");
        restartButton.setPreferredSize(new Dimension(120, 30));
        restartButton.setFocusable(false);
        restartButton.setBackground(new Color(255, 255, 255));
        restartButton.setOpaque(true);
        restartButton.setBorderPainted(false); // ลบเส้นขอบสีขาว
        restartButton.setForeground(Color.BLACK); // เปลี่ยนสีตัวอักษรเป็นสีขาว

        restartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                restartButton.setBackground(new Color(200,200,200));
            }
        
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                restartButton.setBackground(new Color(255,255,255));
            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            resetGame();
            }
        });
        restartGamePanel.add(restartButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
        frame.pack();

        hideCardTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideCards();
            }
        });
        hideCardTimer.setRepeats(false);

        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime = System.currentTimeMillis() - startTime;
                long seconds = (elapsedTime / 1000) % 60;
                long minutes = (elapsedTime / (1000 * 60)) % 60;
                long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
                timeLabel.setText(String.format("Time: %02d:%02d:%02d", hours, minutes, seconds));
            }
        });
    }

    private void showWinScreen() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
    
        // ตั้งค่าให้ขนาดหน้าต่างเท่ากับขนาดของหน้าเล่นเกม
        frame.setSize(boardWidth, boardHeight);
        frame.setPreferredSize(new Dimension(boardWidth, boardHeight));
    
        // ตั้งค่าพื้นหลังเป็นรูปภาพเหมือนหน้าแรก
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/img/winbg.png"));
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new GridBagLayout()); // ใช้ GridBagLayout เพื่อให้จัดวางองค์ประกอบได้ง่ายขึ้น
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // กำหนดระยะห่างระหว่างองค์ประกอบ
    
        // ข้อความ "You Win!" ตรงกลาง
        JLabel winTitleLabel = new JLabel("You Win!", SwingConstants.CENTER);
        winTitleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        winTitleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        backgroundLabel.add(winTitleLabel, gbc);
    
        // เพิ่มช่องว่างระหว่างข้อความ "You Win!" และเวลาที่ใช้ไป
        gbc.gridy++;
        backgroundLabel.add(Box.createVerticalStrut(20), gbc);
    
        // แสดงเวลาที่ใช้ไป
        long seconds = (elapsedTime / 1000) % 60;
        long minutes = (elapsedTime / (1000 * 60)) % 60;
        long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
        JLabel timeTakenLabel = new JLabel(String.format("Time Taken: %02d:%02d:%02d", hours, minutes, seconds), SwingConstants.CENTER);
        timeTakenLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        timeTakenLabel.setForeground(Color.WHITE);
        gbc.gridy++;
        backgroundLabel.add(timeTakenLabel, gbc);
    
        // เพิ่มปุ่ม Play Again และ Exit
        gbc.gridy++;
        backgroundLabel.add(Box.createVerticalStrut(50), gbc);
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
        buttonPanel.setOpaque(false); // ทำให้โปร่งใสเพื่อแสดงพื้นหลัง
    
        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("Arial", Font.PLAIN, 20));
        playAgainButton.setBackground(new Color(255, 255, 255));
        playAgainButton.setForeground(Color.BLACK);
        playAgainButton.setBorderPainted(false);
        playAgainButton.setFocusPainted(false);
    
        playAgainButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                playAgainButton.setBackground(new Color(200, 200, 200));
            }
    
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                playAgainButton.setBackground(new Color(255, 255, 255));
            }
        });
    
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGameScreen();
                resetGame();
            }
        });
        buttonPanel.add(playAgainButton);
    
        // ปุ่ม Exit
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        exitButton.setBackground(new Color(255, 0, 0));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorderPainted(false);
    
        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitButton.setBackground(new Color(200, 0, 0));
            }
    
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitButton.setBackground(new Color(255, 0, 0));
            }
        });
    
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        buttonPanel.add(exitButton);
    
        // เพิ่มปุ่มไปยังพื้นหลัง
        gbc.gridy++;
        backgroundLabel.add(buttonPanel, gbc);
    
        // เพิ่ม backgroundLabel ไปยังหน้าจอหลัก
        frame.add(backgroundLabel, BorderLayout.CENTER);
    
        frame.revalidate();
        frame.repaint();
    }
    
    

    void handleTileClick(ActionEvent e) {
        if (!gameReady) {
            gameReady = true;
            startTime = System.currentTimeMillis();
            gameTimer.start();
        }
        JButton tile = (JButton) e.getSource();
        if (tile.getIcon() == cardBackImageIcon) {
            if (card1Selected == null) {
                card1Selected = tile;
                int index = board.indexOf(card1Selected);
                card1Selected.setIcon(cardSet.get(index).cardImageIcon);
            } else if (card2Selected == null) {
                card2Selected = tile;
                int index = board.indexOf(card2Selected);
                card2Selected.setIcon(cardSet.get(index).cardImageIcon);

                if (card1Selected.getIcon() != card2Selected.getIcon()) {
                    hideCardTimer.start();
                } else {
                    matchedPairs++;
                    card1Selected = null;
                    card2Selected = null;

                    if (matchedPairs == cardSet.size() / 2) {
                        gameTimer.stop();
                        elapsedTime = System.currentTimeMillis() - startTime; // บันทึกเวลาที่ใช้ไว้ก่อนรีเซ็ตเกม
                        showWinScreen(); // แสดงหน้า "You Win!"
                    }
                    
                }
            }
        }
    }

    void resetGame() {
        gameReady = false;
        card1Selected = null;
        card2Selected = null;
        matchedPairs = 0;
    
        // สุ่มไพ่ใหม่
        shuffleCards();
    
        // ลบปุ่มไพ่ทั้งหมดออกและสร้างใหม่
        boardPanel.removeAll();
        board.clear();
        
        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setMinimumSize(new Dimension(cardWidth, cardHeight));
            tile.setMaximumSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(false);
            tile.setContentAreaFilled(false);
            tile.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            tile.setFocusPainted(false);
            tile.setIcon(cardBackImageIcon);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleTileClick(e);
                }
            });
            board.add(tile);
            boardPanel.add(tile);
        }
    
        // รีเซ็ตเวลาและสถานะเกม
        gameTimer.stop();
        elapsedTime = 0;
        startTime = 0;
        timeLabel.setText("Time: 00:00:00");
        winLabel.setText("");
        winLabel.setVisible(false);
    
        // รีเซ็ตตัวนับการซ่อนการ์ด
        hideCardTimer.stop();
    
        // อัปเดตการแสดงผลกระดาน
        boardPanel.revalidate();
        boardPanel.repaint();
    }
    
    
    

    void setupCards() {
        cardSet = new ArrayList<Card>();
        for (String cardName : cardList) {
            Image cardImg = new ImageIcon(getClass().getResource("./img/" + cardName + ".png")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));

            Card card = new Card(cardName, cardImageIcon);
            cardSet.add(card);
        }
        cardSet.addAll(cardSet);

        Image cardBackImg = new ImageIcon(getClass().getResource("./img/BACK.png")).getImage();
        cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }

    void shuffleCards() {
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int) (Math.random() * cardSet.size());
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
    }

    void hideCards() {
        if (card1Selected != null && card2Selected != null) {
            card1Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            card2Selected.setIcon(cardBackImageIcon);
            card2Selected = null;
        }
    }
    public static void main(String[] args) {
        MatchCards matchCards = new MatchCards();
    }
}