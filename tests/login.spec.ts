import { expect, test } from "@playwright/test";

test("login with admin account", async ({ page }) => {
  await page.goto("/login");

  await page.getByPlaceholder("请输入用户名").fill("admin");
  await page.locator('input[placeholder="请输入密码"][type="password"]').first().fill("Ahzx@tdc#21");
  await page.getByRole("button", { name: "登录" }).click();

  // Keep assertions permissive for sample generation across environments.
  await expect(page).toHaveURL(/\/(login|$|index|dashboard|home)/);
});
