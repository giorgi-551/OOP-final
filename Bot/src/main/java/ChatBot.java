import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class BlogPost {
    public String title;
    public String author;
    public String content;
    public String timestamp;
    public int id;

    public BlogPost(String title, String author, String content, String timestamp, int id) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
        this.id = id;
    }
}

class BlogBot {
    private static final String CONFIG_FILE = "config.properties";
    private static final ZoneId TBILISI_ZONE = ZoneId.of("Asia/Tbilisi");
    private String serverUrl;
    private String botName;
    private HttpClient httpClient;
    private Scanner scanner;

    public BlogBot() {
        this.httpClient = HttpClient.newHttpClient();
        this.scanner = new Scanner(System.in);
        loadConfiguration();
    }

    private void loadConfiguration() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            props.load(input);
            this.serverUrl = props.getProperty("server.url", "http://max.ge/q45/46192837/index.php");
            this.botName = props.getProperty("bot.name", "BlogBot");
        } catch (IOException e) {
            System.out.println("⚠️  Configuration file not found. Using default values.");
            System.out.println("Creating default config.properties file...");
            createDefaultConfig();
        }
    }

    private void createDefaultConfig() {
        Properties props = new Properties();
        props.setProperty("server.url", "http://max.ge/q45/46192837/index.php");
        props.setProperty("bot.name", "BlogBot");

        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            props.store(output, "BlogBot Configuration");
            this.serverUrl = "http://max.ge/q45/46192837/index.php";
            this.botName = "BlogBot";
            System.out.println("✅ Default configuration created successfully!");
        } catch (IOException e) {
            System.err.println("❌ Error creating configuration file: " + e.getMessage());
        }
    }

    public void start() {
        System.out.println(repeatString("=", 50));
        System.out.println("🤖 Welcome to " + botName + "!");
        System.out.println("🌐 Server: " + serverUrl);
        System.out.println(repeatString("=", 50));

        while (true) {
            showMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    createBlogPost();
                    break;
                case 2:
                    viewAllBlogPosts();
                    break;
                case 3:
                    deleteBlogPost();
                    break;
                case 4:
                    viewSiteStatistics();
                    break;
                case 5:
                    System.out.println("👋 Goodbye!");
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please select 1-5.");
                    break;
            }
        }
    }

    private void showMenu() {
        System.out.println("\n" + repeatString("=", 50));
        System.out.println(botName + ": What would you like to do?");
        System.out.println(repeatString("=", 50));
        System.out.println("1. ✏️  Create a new blog post");
        System.out.println("2. 📖  View all blog posts");
        System.out.println("3. 🗑️  Delete a blog post");
        System.out.println("4. 📊  View site statistics");
        System.out.println("5. 🚪  Exit");
        System.out.println(repeatString("=", 50));
        System.out.print("Your choice (1-5): ");
    }

    private int getUserChoice() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void createBlogPost() {
        System.out.println("\n" + botName + ": Let's create a new blog post!");
        System.out.println(repeatString("-", 40));

        System.out.print("📝 Enter blog title: ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("❌ Title cannot be empty!");
            return;
        }

        System.out.print("👤 Enter author name (or press Enter to post Anonymous): ");
        String author = scanner.nextLine().trim();
        if (author.isEmpty()) {
            author = "Anonymous";
        }

        System.out.println("📄 Enter blog content (type 'END' on a new line to finish):");
        StringBuilder content = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals("END")) {
            if (content.length() > 0) {
                content.append("\n");
            }
            content.append(line);
        }

        if (content.toString().trim().isEmpty()) {
            System.out.println("❌ Content cannot be empty!");
            return;
        }

        String jsonPayload = String.format(
                "{\"title\":\"%s\",\"author\":\"%s\",\"content\":\"%s\"}",
                escapeJson(title), escapeJson(author), escapeJson(content.toString())
        );

        try {
            System.out.println("\n" + botName + ": Creating blog post...");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "?api=blogs"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                System.out.println("✅ " + botName + ": Blog post created successfully!");
            } else if (response.statusCode() == 400) {
                System.out.println("⚠️ " + botName + ": Bad request - check your input or server limits");
            } else {
                System.out.println("❌ " + botName + ": Failed to create blog post (Status: " + response.statusCode() + ")");
            }

        } catch (Exception e) {
            System.err.println("❌ Error creating blog post: " + e.getMessage());
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void viewAllBlogPosts() {
        try {
            System.out.println("\n" + botName + ": Fetching all blog posts...");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "?api=blogs"))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            if (response.statusCode() == 200 && responseBody.contains("\"data\"")) {
                System.out.println("✅ " + botName + ": Blog posts retrieved successfully!");
                System.out.println(repeatString("=", 50));
                parseAndDisplayBlogsSorted(responseBody);
            } else if (responseBody.contains("<!DOCTYPE html>")) {
                System.out.println("⚠️ " + botName + ": Server returned HTML instead of JSON");
                System.out.println("💡 This might indicate an API configuration issue on the server");
            } else {
                System.out.println("❌ " + botName + ": Failed to retrieve blog posts (Status: " + response.statusCode() + ")");
            }

        } catch (Exception e) {
            System.err.println("❌ Error fetching blog posts: " + e.getMessage());
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void deleteBlogPost() {
        System.out.println("\n" + botName + ": Delete a blog post");
        System.out.println(repeatString("-", 40));

        // First, get and display all blog posts with their IDs
        List<BlogPost> blogPosts = fetchBlogPosts();

        if (blogPosts.isEmpty()) {
            System.out.println("📝 " + botName + ": No blog posts found to delete.");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        // Display posts with numbers for selection
        System.out.println("📋 Available blog posts:");
        System.out.println(repeatString("-", 40));

        for (int i = 0; i < blogPosts.size(); i++) {
            BlogPost post = blogPosts.get(i);
            System.out.println((i + 1) + ". " + (post.title.isEmpty() ? "(No title)" : post.title));
            System.out.println("   👤 Author: " + (post.author.isEmpty() ? "Anonymous" : post.author));
            if (post.id != 0) {
                System.out.println("   🆔 ID: " + post.id);
            }
            if (!post.timestamp.isEmpty()) {
                String tbilisiTime = convertToTbilisiTime(post.timestamp);
                System.out.println("   🕐 Created: " + tbilisiTime);
            }
            System.out.println(repeatString("-", 40));
        }

        System.out.print("🗑️  Enter the number of the post to delete (1-" + blogPosts.size() + ") or 0 to cancel: ");

        try {
            String input = scanner.nextLine().trim();
            int choice = Integer.parseInt(input);

            if (choice == 0) {
                System.out.println("❌ Deletion cancelled.");
                return;
            }

            if (choice < 1 || choice > blogPosts.size()) {
                System.out.println("❌ Invalid choice. Please select a number between 1 and " + blogPosts.size());
                return;
            }

            BlogPost selectedPost = blogPosts.get(choice - 1);

            // Show confirmation
            System.out.println("\n⚠️  You are about to delete:");
            System.out.println("📝 Title: " + (selectedPost.title.isEmpty() ? "(No title)" : selectedPost.title));
            System.out.println("👤 Author: " + (selectedPost.author.isEmpty() ? "Anonymous" : selectedPost.author));
            System.out.print("\n❓ Are you sure you want to delete this post? (yes/no): ");

            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (!confirmation.equals("yes") && !confirmation.equals("y")) {
                System.out.println("❌ Deletion cancelled.");
                return;
            }

            // Attempt to delete the post
            performDeleteRequest(selectedPost);

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a number.");
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private List<BlogPost> fetchBlogPosts() {
        List<BlogPost> blogPosts = new ArrayList<>();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "?api=blogs"))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 && response.body().contains("\"data\"")) {
                blogPosts = parseBlogPosts(response.body());
            }
        } catch (Exception e) {
            System.err.println("❌ Error fetching blog posts: " + e.getMessage());
        }

        return blogPosts;
    }

    private List<BlogPost> parseBlogPosts(String jsonResponse) {
        List<BlogPost> blogPosts = new ArrayList<>();

        try {
            String dataArray = "";

            Pattern arrayPattern = Pattern.compile("\"data\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
            Matcher arrayMatcher = arrayPattern.matcher(jsonResponse);
            if (arrayMatcher.find()) {
                dataArray = arrayMatcher.group(1);
            } else {
                return blogPosts;
            }

            Pattern blogPattern = Pattern.compile("\\{([^{}]*(?:\\{[^{}]*\\}[^{}]*)*)\\}");
            Matcher blogMatcher = blogPattern.matcher(dataArray);

            while (blogMatcher.find()) {
                String blogJson = "{" + blogMatcher.group(1) + "}";

                String title = extractJsonValue(blogJson, "title");
                String author = extractJsonValue(blogJson, "author");
                String content = extractJsonValue(blogJson, "content");
                String timestamp = extractJsonValue(blogJson, "created_at");
                if (timestamp.isEmpty()) {
                    timestamp = extractJsonValue(blogJson, "timestamp");
                }
                if (timestamp.isEmpty()) {
                    timestamp = extractJsonValue(blogJson, "date");
                }

                String idStr = extractJsonValue(blogJson, "id");
                int id = 0;
                try {
                    if (!idStr.isEmpty()) {
                        id = Integer.parseInt(idStr);
                    }
                } catch (NumberFormatException e) {
                    // Keep id as 0 if parsing fails
                }

                if (!title.isEmpty() || !content.isEmpty()) {
                    blogPosts.add(new BlogPost(title, author, content, timestamp, id));
                }
            }

            sortBlogPosts(blogPosts);
        } catch (Exception e) {
            System.out.println("⚠️ Error parsing blog data: " + e.getMessage());
        }

        return blogPosts;
    }

    private void performDeleteRequest(BlogPost post) {
        try {
            System.out.println("\n" + botName + ": Deleting blog post...");

            String deleteUrl;
            if (post.id != 0) {
                // If we have an ID, use it
                deleteUrl = serverUrl + "?api=blogs&id=" + post.id;
            } else {
                // Fallback: try to delete by title (URL encode the title)
                String encodedTitle = java.net.URLEncoder.encode(post.title, "UTF-8");
                deleteUrl = serverUrl + "?api=blogs&title=" + encodedTitle;
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(deleteUrl))
                    .header("Accept", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("✅ " + botName + ": Blog post deleted successfully!");
            } else if (response.statusCode() == 404) {
                System.out.println("⚠️ " + botName + ": Blog post not found. It may have been already deleted.");
            } else if (response.statusCode() == 403) {
                System.out.println("⚠️ " + botName + ": Permission denied. You may not be allowed to delete this post.");
            } else {
                System.out.println("❌ " + botName + ": Failed to delete blog post (Status: " + response.statusCode() + ")");
                System.out.println("Response: " + response.body());
            }

        } catch (Exception e) {
            System.err.println("❌ Error deleting blog post: " + e.getMessage());
        }
    }

    private void viewSiteStatistics() {
        try {
            System.out.println("\n" + botName + ": Fetching site statistics...");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "?api=stats"))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            if (response.statusCode() == 200 && (responseBody.contains("total_posts") || responseBody.contains("success"))) {
                System.out.println("✅ " + botName + ": Site Statistics:");
                System.out.println(repeatString("=", 40));
                parseAndDisplayStats(responseBody);
            } else if (responseBody.contains("<!DOCTYPE html>")) {
                System.out.println("⚠️ " + botName + ": Server returned HTML instead of JSON");
                System.out.println("💡 This might indicate an API configuration issue on the server");
            } else {
                System.out.println("❌ " + botName + ": Failed to retrieve statistics (Status: " + response.statusCode() + ")");
            }

        } catch (Exception e) {
            System.err.println("❌ Error fetching statistics: " + e.getMessage());
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private String repeatString(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    private String extractJsonValue(String json, String key) {
        // First try to match quoted strings
        Pattern quotedPattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*?)\"");
        Matcher quotedMatcher = quotedPattern.matcher(json);
        if (quotedMatcher.find()) {
            return quotedMatcher.group(1);
        }

        Pattern unquotedPattern = Pattern.compile("\"" + key + "\"\\s*:\\s*([^,}\\]\\s]+)");
        Matcher unquotedMatcher = unquotedPattern.matcher(json);
        if (unquotedMatcher.find()) {
            return unquotedMatcher.group(1).trim();
        }

        return "";
    }

    private void parseAndDisplayBlogsSorted(String jsonResponse) {
        try {
            List<BlogPost> blogPosts = parseBlogPosts(jsonResponse);

            if (blogPosts.isEmpty()) {
                System.out.println("📝 " + botName + ": No blog posts found.");
            } else {
                for (int i = 0; i < blogPosts.size(); i++) {
                    BlogPost post = blogPosts.get(i);
                    System.out.println("📝 Blog Post #" + (i + 1));
                    System.out.println("🏷️  Title: " + (post.title.isEmpty() ? "(No title)" : post.title));
                    System.out.println("👤 Author: " + (post.author.isEmpty() ? "Anonymous" : post.author));
                    System.out.println("📄 Content: " + (post.content.isEmpty() ? "(No content)" : post.content));
                    if (post.id != 0) {
                        System.out.println("🆔 ID: " + post.id);
                    }
                    if (!post.timestamp.isEmpty()) {
                        String tbilisiTime = convertToTbilisiTime(post.timestamp);
                        System.out.println("🕐 Created: " + tbilisiTime);
                    }
                    System.out.println(repeatString("-", 40));
                }
                System.out.println("📊 Total: " + blogPosts.size() + " blog post(s)");
            }

        } catch (Exception e) {
            System.out.println("⚠️ Error parsing blog data: " + e.getMessage());
        }
    }

    private void sortBlogPosts(List<BlogPost> blogPosts) {
        blogPosts.sort((post1, post2) -> {
            if (!post1.timestamp.isEmpty() && !post2.timestamp.isEmpty()) {
                return post2.timestamp.compareTo(post1.timestamp); // Descending (newest first)
            }

            if (post1.id != 0 && post2.id != 0) {
                return Integer.compare(post2.id, post1.id); // Descending (highest ID first)
            }

            String title1Numbers = extractNumbersFromTitle(post1.title);
            String title2Numbers = extractNumbersFromTitle(post2.title);

            if (!title1Numbers.isEmpty() && !title2Numbers.isEmpty()) {
                try {
                    int num1 = Integer.parseInt(title1Numbers);
                    int num2 = Integer.parseInt(title2Numbers);
                    return Integer.compare(num2, num1);
                } catch (NumberFormatException e) {
                    // Fall through to default sorting
                }
            }

            return post1.title.compareToIgnoreCase(post2.title);
        });
    }

    private String extractNumbersFromTitle(String title) {
        Pattern numberPattern = Pattern.compile("#(\\d+)");
        Matcher matcher = numberPattern.matcher(title);
        if (matcher.find()) {
            return matcher.group(1);
        }

        Pattern endNumberPattern = Pattern.compile("(\\d+)$");
        Matcher endMatcher = endNumberPattern.matcher(title.trim());
        if (endMatcher.find()) {
            return endMatcher.group(1);
        }

        return "";
    }

    private void parseAndDisplayStats(String jsonResponse) {
        try {
            String dataSection = jsonResponse;

            Pattern dataPattern = Pattern.compile("\"data\"\\s*:\\s*\\{([^}]+)\\}");
            Matcher dataMatcher = dataPattern.matcher(jsonResponse);
            if (dataMatcher.find()) {
                dataSection = "{" + dataMatcher.group(1) + "}";
            }

            String totalPosts = extractJsonValue(dataSection, "total_posts");
            String maxPosts = extractJsonValue(dataSection, "max_posts");
            String remainingPosts = extractJsonValue(dataSection, "remaining_posts");
            String percentageUsed = extractJsonValue(dataSection, "percentage_used");
            String canAddMore = extractJsonValue(dataSection, "can_add_more");

            // Display available statistics
            boolean hasStats = false;

            if (!totalPosts.isEmpty()) {
                System.out.println("📊 Total Posts: " + totalPosts);
                hasStats = true;
            }
            if (!maxPosts.isEmpty()) {
                System.out.println("🎯 Max Posts: " + maxPosts);
                hasStats = true;
            }
            if (!remainingPosts.isEmpty()) {
                System.out.println("📈 Remaining: " + remainingPosts);
                hasStats = true;
            }
            if (!percentageUsed.isEmpty()) {
                System.out.println("📉 Usage: " + percentageUsed + "%");
                hasStats = true;
            }
            if (!canAddMore.isEmpty()) {
                System.out.println("✅ Can Add More: " + canAddMore);
                hasStats = true;
            }

            if (!hasStats) {
                System.out.println("⚠️ No statistics found in response.");
            }

        } catch (Exception e) {
            System.out.println("⚠️ Error parsing statistics: " + e.getMessage());
        }
    }

    private String convertToTbilisiTime(String timestamp) {
        if (timestamp == null || timestamp.trim().isEmpty()) {
            return timestamp;
        }

        try {
            ZonedDateTime zonedDateTime = null;

            try {
                if (timestamp.endsWith("Z")) {
                    Instant instant = Instant.parse(timestamp);
                    zonedDateTime = instant.atZone(TBILISI_ZONE);
                } else if (timestamp.contains("+") || timestamp.matches(".*-\\d{2}:\\d{2}$")) {
                    zonedDateTime = ZonedDateTime.parse(timestamp).withZoneSameInstant(TBILISI_ZONE);
                }
            } catch (DateTimeParseException e) {
                // Try next format
            }

            if (zonedDateTime == null) {
                try {
                    LocalDateTime localDateTime = LocalDateTime.parse(timestamp.replace(" ", "T"));
                    zonedDateTime = localDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(TBILISI_ZONE);
                } catch (DateTimeParseException e) {
                    // Try next format
                }
            }

            if (zonedDateTime == null) {
                try {
                    DateTimeFormatter mysqlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime localDateTime = LocalDateTime.parse(timestamp, mysqlFormatter);
                    zonedDateTime = localDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(TBILISI_ZONE);
                } catch (DateTimeParseException e) {
                    // Try next format
                }
            }

            if (zonedDateTime == null) {
                try {
                    long unixTimestamp = Long.parseLong(timestamp);
                    // Handle both seconds and milliseconds
                    if (unixTimestamp > 1000000000000L) {
                        // Milliseconds
                        Instant instant = Instant.ofEpochMilli(unixTimestamp);
                        zonedDateTime = instant.atZone(TBILISI_ZONE);
                    } else {
                        // Seconds
                        Instant instant = Instant.ofEpochSecond(unixTimestamp);
                        zonedDateTime = instant.atZone(TBILISI_ZONE);
                    }
                } catch (NumberFormatException e) {
                    // Not a unix timestamp
                }
            }

            if (zonedDateTime != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' h:mm a");
                return zonedDateTime.format(formatter);
            }

        } catch (Exception e) {
            // If all parsing fails, return original timestamp with note
            return timestamp + " (original format)";
        }

        return timestamp + " (unknown format)";
    }

    private String escapeJson(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public static void main(String[] args) {
        BlogBot bot = new BlogBot();
        bot.start();
    }
}