// 클래스나 메서드를 감싸서 기능을 확장
class UserService {
  @Log
  getUser(name: string): string {
    return `User: ${name}`;
  }
}

const service = new UserService();
service.getUser("Alex");

// TypeScript에서 메서드 데코레이터의 시그니처
// function decorator(
//   target: any,
//   propertyKey: string,
//   descriptor: PropertyDescriptor
// ) 

// 로그 데코레이터
function Log(
  target: any, // 어디에 붙어있는지
  propertyKey: string, // 메서드 이름 (getUser)
  descriptor: PropertyDescriptor // { value: Function, writable: boolean, enumerable: boolean, configurable: boolean }
) {
  const originalMethod = descriptor.value; // 백업

  // 함수 교체
  descriptor.value = function (...args: any[]) {
    console.log(`[LOG] ${propertyKey} called with`, args);

    return originalMethod.apply(this, args); // this 유지하면서 함수 실행
  }

  return descriptor;
}
